package com.example.finconnect.controller;

import com.example.finconnect.model.Post;
import com.example.finconnect.model.PostPatchRequestBody;
import com.example.finconnect.model.PostPostRequestBody;
import com.example.finconnect.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.inject.Inject;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    private final PostService postService;

    @Inject
    public PostController(PostService postService) {
        this.postService = postService; }

    @GetMapping
    public ResponseEntity<List<Post>> getPosts() {
        logger.info("GET /api/v1/posts");
        var posts = postService.getPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostByPostId(@PathVariable Long postId) {
        logger.info("GET /api/v1/posts/{}", postId);
        var post = postService.getPostByPostId(postId);
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<Post> creatPost(@RequestBody PostPostRequestBody postPostRequestBody) {
        logger.info("POST /api/v1/posts");
        var post = postService.creatPost(postPostRequestBody);
        return ResponseEntity.ok(post);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long postId, @RequestBody PostPatchRequestBody postPatchRequestBody) {
        logger.info("PATCH /api/v1/posts/{}", postId);
        var post = postService.updatePost(postId, postPatchRequestBody);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        logger.info("DELETE /api/v1/posts/{}", postId);
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
