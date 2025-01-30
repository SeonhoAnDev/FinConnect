package com.example.finconnect.service;

import com.example.finconnect.exception.follow.FollowAlreadyExistsException;
import com.example.finconnect.exception.follow.FollowNotFoundException;
import com.example.finconnect.exception.follow.InvalidFollowException;
import com.example.finconnect.exception.post.PostNotFoundException;
import com.example.finconnect.exception.user.UserAlreadyExistsException;
import com.example.finconnect.exception.user.UserNotAllowedException;
import com.example.finconnect.exception.user.UserNotFoundException;
import com.example.finconnect.model.entity.FollowEntity;
import com.example.finconnect.model.entity.LikeEntity;
import com.example.finconnect.model.entity.PostEntity;
import com.example.finconnect.model.entity.UserEntity;
import com.example.finconnect.model.user.*;
import com.example.finconnect.repository.FollowEntityRepository;
import com.example.finconnect.repository.LikeEntityRepository;
import com.example.finconnect.repository.PostEntityRepository;
import com.example.finconnect.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private FollowEntityRepository followEntityRepository;

    @Autowired
    private PostEntityRepository postEntityRepository;

    @Autowired
    private LikeEntityRepository likeEntityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public User signUp(String username, String password) {
        userEntityRepository
                .findByUsername(username)
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException();
                });
        var userEntity = userEntityRepository.save(UserEntity.of(username, passwordEncoder.encode(password)));

        return User.from(userEntity);
    }

    public UserAuthenticationResponse authenticate(String username, String password) {
        var userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        if (passwordEncoder.matches(password, userEntity.getPassword())) {
            var accessToken = jwtService.generateAccessToken(userEntity);
            return new UserAuthenticationResponse(accessToken);
        } else {
            throw new UserNotFoundException();
        }
    }

    public List<User> getUsers(String query, UserEntity currentUser) {
        List<UserEntity> userEntities;
        if (query != null && !query.isBlank()) {
            //特定ユーザー検索機能　queryを元にusernameで検索
            userEntities = userEntityRepository.findByUsernameContaining(query);
        } else {
            userEntities = userEntityRepository.findAll();
        }
        return userEntities.stream()
                .map(userEntity -> getUserWithFollowingStatus(userEntity, currentUser))
                .toList();
    }

    public User getUser(String username, UserEntity currentUser) {
        var userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return getUserWithFollowingStatus(userEntity, currentUser);
    }

    private User getUserWithFollowingStatus(UserEntity userEntity, UserEntity currentUser) {
        var isFollowing =
                followEntityRepository.findByFollowerAndFollowing(currentUser, userEntity).isPresent();
        return User.from(userEntity, isFollowing);
    }

    public User updateUser(
            String username, UserPatchRequestBody userPatchRequestBody, UserEntity currentUser) {
        var userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        if (!userEntity.equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        if (userPatchRequestBody.description() != null) {
            userEntity.setDesription(userPatchRequestBody.description());
        }

        return User.from(userEntityRepository.save(userEntity));
    }

    @Transactional
    public User follow(String username, UserEntity currentUser) {
        var following =
                userEntityRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new UserNotFoundException(username));

        if (following.equals(currentUser)) {
            throw new InvalidFollowException("A user cannot follow themselves.");
        }

        followEntityRepository.findByFollowerAndFollowing(currentUser, following)
                .ifPresent(followEntity -> {
                    throw new FollowAlreadyExistsException(currentUser, following);
                });
        followEntityRepository.save(
                FollowEntity.of(currentUser, following)
        );

        following.setFollowersCount(following.getFollowersCount() + 1);
        currentUser.setFollowingsCount(following.getFollowingsCount() + 1);

        userEntityRepository.saveAll(List.of(following, currentUser));

        return User.from(following, true);
    }

    @Transactional
    public User unfollow(String username, UserEntity currentUser) {
        var following =
                userEntityRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new UserNotFoundException(username));

        if (following.equals(currentUser)) {
            throw new InvalidFollowException("A user cannot unfollow themselves.");
        }

        var followEntity = followEntityRepository
                .findByFollowerAndFollowing(currentUser, following)
                .orElseThrow(
                        () -> new FollowNotFoundException(currentUser, following));
        followEntityRepository.delete(followEntity);

        following.setFollowersCount(Math.max(0, following.getFollowersCount() - 1));
        currentUser.setFollowingsCount(Math.max(0, following.getFollowingsCount() - 1));

        userEntityRepository.saveAll(List.of(following, currentUser));

        return User.from(following, false);
    }

    public List<Follower> getFollowersByUsername(String username, UserEntity currentUser) {
        var following =
                userEntityRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new UserNotFoundException(username));
        var followEntities = followEntityRepository.findByFollowing(following);
        return followEntities.stream()
                .map(follow -> Follower.from(
                        getUserWithFollowingStatus(follow.getFollower(), currentUser),
                        follow.getCreatedDateTime()
                ))
                .toList();
    }

    public List<User> getFollowingsByUsername(String username, UserEntity currentUser) {
        var follower =
                userEntityRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new UserNotFoundException(username));
        var followEntities = followEntityRepository.findByFollower(follower);
        return followEntities.stream()
                .map(follow -> getUserWithFollowingStatus(follow.getFollower(), currentUser))
                .toList();
    }

    public List<LikedUser> getLikedUsersByPostId(Long postId, UserEntity currentUser) {
        var postEntity =
                postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        var likeEntities = likeEntityRepository.findByPost(postEntity);
        return likeEntities.stream()
                .map(
                        likeEntity ->
                            getLikedUserWithFollowingStatus(likeEntity, postEntity, currentUser))
                .toList();
    }
    private  LikedUser getLikedUserWithFollowingStatus(
            LikeEntity likeEntity, PostEntity postEntity, UserEntity currentUser) {
        var likedUserEntity = likeEntity.getUser();
        var userWithFollowingStatus =
                getUserWithFollowingStatus(likedUserEntity, currentUser);
        return LikedUser.from(
                userWithFollowingStatus, postEntity.getPostId(), likeEntity.getCreatedDateTime());
    }

    public List<LikedUser> getLikedUsersByUser(String username, UserEntity currentUser) {
        var userEntity =
                userEntityRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        var postEntities = postEntityRepository.findByUser(userEntity);
        return postEntities.stream()
                .flatMap(
                        postEntity ->
                                likeEntityRepository.findByPost(postEntity).stream()
                                        .map(
                                                likeEntity ->
                                                        getLikedUserWithFollowingStatus(likeEntity, postEntity, currentUser)))
                .toList();
    }
}
