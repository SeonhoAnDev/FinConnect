package com.example.finconnect.service;

import com.example.finconnect.exception.post.PostNotFoundException;
import com.example.finconnect.exception.user.UserNotAllowedException;
import com.example.finconnect.model.entity.LikeEntity;
import com.example.finconnect.model.entity.PostEntity;
import com.example.finconnect.model.entity.UserEntity;
import com.example.finconnect.model.post.Post;
import com.example.finconnect.model.post.PostPatchRequestBody;
import com.example.finconnect.model.post.PostPostRequestBody;
import com.example.finconnect.repository.LikeEntityRepository;
import com.example.finconnect.repository.PostEntityRepository;
import com.example.finconnect.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    private static final List<Post> posts = new ArrayList<>();
    @Autowired
    private PostEntityRepository postEntityRepository;
    @Autowired
    private UserEntityRepository userEntityRepository;
    @Autowired
    private LikeEntityRepository likeEntityRepository;

    public List<Post> getPosts(UserEntity currentUser) {
        var postEntities = postEntityRepository.findAll();

        return postEntities.stream()
                .map(PostEntity -> getPostWithLikingStatus(PostEntity, currentUser))
                .toList();
    }

    public Post getPostByPostId(Long postId, UserEntity currentUser) throws PostNotFoundException {
        var postEntity =
                postEntityRepository
                        .findById(postId)
                        .orElseThrow(
                                () -> new PostNotFoundException(postId));

        return getPostWithLikingStatus(postEntity, currentUser);
    }

    private Post getPostWithLikingStatus(PostEntity postEntity, UserEntity currentUser) {
        var isLiking = likeEntityRepository.findByUserAndPost(currentUser, postEntity).isPresent();
        return Post.from(postEntity, isLiking);
    }

    public Post creatPost(PostPostRequestBody postpostRequestBody, UserEntity currentUser) {
        var savedPostEntity =
                postEntityRepository.save(
                        PostEntity.of(postpostRequestBody.body(), currentUser)
                );
        return Post.from(savedPostEntity);
    }

    public Post updatePost(Long postId, PostPatchRequestBody postpatchRequestBody, UserEntity currentUser) {
        var postEntity =
                postEntityRepository
                        .findById(postId)
                        .orElseThrow(
                                () -> new PostNotFoundException(postId));
        if (!postEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }
        postEntity.setBody(postpatchRequestBody.body());
        var savedPostEntity = postEntityRepository.save(postEntity);
        return Post.from(savedPostEntity);
    }

    public void deletePost(Long postId, UserEntity currentUser) {
        var postEntity =
                postEntityRepository
                        .findById(postId)
                        .orElseThrow(
                                () -> new PostNotFoundException(postId));

        if (!postEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        postEntityRepository.delete(postEntity);
    }

    public List<Post> getPostByUsername(String username, UserEntity currentUser) {
        var userEntity =
                userEntityRepository
                        .findByUsername(username)
                        .orElseThrow(
                                () -> new PostNotFoundException(username));

        var postEntities = postEntityRepository.findByUser(userEntity);
        return postEntities.stream()
                .map(PostEntity -> getPostWithLikingStatus(PostEntity, currentUser))
                .toList();
    }

    @Transactional
    public Post toggleLike(Long postId, UserEntity currentUser) {
        var postEntity =
                postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        var likeEntity = likeEntityRepository.findByUserAndPost(currentUser, postEntity);
        if (likeEntity.isPresent()) {
            likeEntityRepository.delete(likeEntity.get());
            postEntity.setLikesCount(Math.max(0, postEntity.getLikesCount() - 1));
            return Post.from(postEntityRepository.save(postEntity), false);
        } else {
            likeEntityRepository.save(LikeEntity.of(currentUser, postEntity));
            postEntity.setLikesCount(postEntity.getLikesCount() + 1);
            return Post.from(postEntityRepository.save(postEntity), true);
        }
    }
}
