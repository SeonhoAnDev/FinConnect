package com.example.finconnect.controller;

import com.example.finconnect.model.Post;
import com.example.finconnect.model.PostPostRequestBody;
import com.example.finconnect.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import javax.inject.Inject;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @Inject
    public PostController(PostService postService) {
        this.postService = postService; }

    @GetMapping
    public ResponseEntity<List<Post>> getPosts() {
        List<Post> posts = postService.getPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostByPostId(@PathVariable Long postId) {
        Optional<Post> matchingPost = postService.getPostId(postId);
        return matchingPost
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .notFound()
                        .build());
    }

    @PostMapping
    public ResponseEntity<Post> creatPost(@RequestBody PostPostRequestBody postPostRequestBody) {
        var post = postService.creatPost(postPostRequestBody);
        return ResponseEntity.ok(post);
    }
}
