package com.example.finconnect.service;

import com.example.finconnect.exception.follow.FollowAlreadyExistsException;
import com.example.finconnect.exception.follow.FollowNotFoundException;
import com.example.finconnect.exception.follow.InvalidFollowException;
import com.example.finconnect.exception.user.UserAlreadyExistsException;
import com.example.finconnect.exception.user.UserNotAllowedException;
import com.example.finconnect.exception.user.UserNotFoundException;
import com.example.finconnect.model.entity.FollowEntity;
import com.example.finconnect.model.entity.UserEntity;
import com.example.finconnect.model.user.User;
import com.example.finconnect.model.user.UserAuthenticationResponse;
import com.example.finconnect.model.user.UserPatchRequestBody;
import com.example.finconnect.repository.FollowEntityRepository;
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

    public List<User> getUsers(String query) {
        List<UserEntity> userEntities;
        if (query != null && !query.isBlank()) {
            //特定ユーザー検索機能　queryを元にusernameで検索
            userEntities = userEntityRepository.findByUsernameContaining(query);
        } else {
            userEntities = userEntityRepository.findAll();
        }
        return userEntities.stream().map(User::from).toList();
    }

    public User getUser(String username) {
        var userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return User.from(userEntity);
    }

    public User updateUser(String username, UserPatchRequestBody userPatchRequestBody, UserEntity currentUser) {
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

        return User.from(following);
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

        return User.from(following);
    }
}
