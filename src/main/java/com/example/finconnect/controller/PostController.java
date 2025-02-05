package com.example.finconnect.controller;

import com.example.finconnect.model.entity.UserEntity;
import com.example.finconnect.model.post.Post;
import com.example.finconnect.model.post.PostPatchRequestBody;
import com.example.finconnect.model.post.PostPostRequestBody;
import com.example.finconnect.model.user.LikedUser;
import com.example.finconnect.service.PostService;
import com.example.finconnect.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired private PostService postService;

    @Autowired private UserService userService;

    @Inject
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getPosts(Authentication authentication) {
        logger.info("GET /api/v1/posts");
        var posts = postService.getPosts((UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostByPostId(
            @PathVariable Long postId, Authentication authentication) {
        logger.info("GET /api/v1/posts/{}", postId);
        var post = postService.getPostByPostId(postId, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(post);
    }

    @GetMapping("/{postId}/liked-users")
    public ResponseEntity<List<LikedUser>> getLikedUserPostByPostId(
            @PathVariable Long postId, Authentication authentication) {
        var likedUser = userService.getLikedUsersByPostId(
                postId, (UserEntity) authentication.getPrincipal()
        );
        return ResponseEntity.ok(likedUser);
    }

    @PostMapping
    public ResponseEntity<Post> creatPost(
            @RequestBody PostPostRequestBody postPostRequestBody, Authentication authentication) {
        logger.info("POST /api/v1/posts");
        var post = postService.creatPost(postPostRequestBody, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(post);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long postId, @RequestBody PostPatchRequestBody postPatchRequestBody, Authentication authentication) {
        logger.info("PATCH /api/v1/posts/{}", postId);
        var post = postService.updatePost(postId, postPatchRequestBody, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, Authentication authentication) {
        logger.info("DELETE /api/v1/posts/{}", postId);
        postService.deletePost(postId, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/likes")
    public ResponseEntity<Post> toggleLike(@PathVariable Long postId, Authentication authentication) {
        var post = postService.toggleLike(postId, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(post);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Post>> searchPosts(
            @RequestParam String keyword,
            Authentication authentication) {
        var posts = postService.searchPostsByBody(
                keyword,
                (UserEntity) authentication.getPrincipal()
        );
        return ResponseEntity.ok(posts);
    }
}
