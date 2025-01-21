package com.example.finconnect.service;

import com.example.finconnect.exception.post.PostNotFoundException;
import com.example.finconnect.model.Post;
import com.example.finconnect.model.PostPatchRequestBody;
import com.example.finconnect.model.PostPostRequestBody;
import com.example.finconnect.model.entity.PostEntity;
import com.example.finconnect.repository.PostEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    private static final List<Post> posts = new ArrayList<>();
    @Autowired
    private PostEntityRepository postEntityRepository;

    public List<Post> getPosts() {
        var postEntities = postEntityRepository.findAll();

        return postEntities.stream().map(Post::from).toList();
    }

    public Post getPostByPostId(Long postId) {

        var postEntity =
                postEntityRepository
                        .findById(postId)
                        .orElseThrow(
                                () -> new PostNotFoundException(postId));
        return Post.from(postEntity);
    }

    public Post creatPost(PostPostRequestBody postpostRequestBody) {
        var postEntity = new PostEntity();
        postEntity.setBody(postpostRequestBody.body());
        var savedPostEntity = postEntityRepository.save(postEntity);
        return Post.from(savedPostEntity);
    }

    public Post updatePost(Long postId, PostPatchRequestBody postpatchRequestBody) {
        var postEntity =
                postEntityRepository
                        .findById(postId)
                        .orElseThrow(
                                () -> new PostNotFoundException(postId));
        postEntity.setBody(postpatchRequestBody.body());
        var savedPostEntity = postEntityRepository.save(postEntity);
        return Post.from(savedPostEntity);
    }

    public void deletePost(Long postId) {
        var postEntity =
                postEntityRepository
                        .findById(postId)
                        .orElseThrow(
                                () -> new PostNotFoundException(postId));
        postEntityRepository.delete(postEntity);
    }
}
