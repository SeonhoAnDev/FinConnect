package com.example.finconnect.controller;

import com.example.finconnect.model.entity.UserEntity;
import com.example.finconnect.model.post.Post;
import com.example.finconnect.model.reply.Reply;
import com.example.finconnect.model.user.*;
import com.example.finconnect.service.PostService;
import com.example.finconnect.service.ReplyService;
import com.example.finconnect.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    ReplyService replyService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            Authentication authentication) {
        final int PAGE_SIZE = 20;
        var pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("createddatetime").descending());
        
        var userPage = userService.getUsers(query, (UserEntity) authentication.getPrincipal(), pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("content", userPage.getContent());
        response.put("totalPages", userPage.getTotalPages());
        response.put("hasNext", userPage.hasNext());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUsers(
            @PathVariable String username, Authentication authentication) {
        var user = userService.getUser(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{username}")
    public ResponseEntity<User> updateUser(@PathVariable String username,
                                           @RequestBody UserPatchRequestBody requestBody,
                                           Authentication authentication) {
        var user = userService.updateUser(username, requestBody, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }

    //URL構造がUSERであるため、USER APIとして作成
    @GetMapping("/{username}/posts")
    public ResponseEntity<List<Post>> getPostsByUsername(
            @PathVariable String username, Authentication authentication) {
        var posts =postService.getPostByUsername(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/{username}/follows")
    public ResponseEntity<User> follow(
            @PathVariable String username, Authentication authentication) {
        var user = userService.follow(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{username}/follows")
    public ResponseEntity<User> unfollow(
            @PathVariable String username, Authentication authentication) {
        var user = userService.unfollow(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<List<Follower>> getFollowersByUser(
            @PathVariable String username, Authentication authentication) {
        var followers = userService.getFollowersByUsername(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{username}/followings")
    public ResponseEntity<List<User>> getFollowingsByUser(
            @PathVariable String username, Authentication authentication) {
        var followings = userService.getFollowingsByUsername(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(followings);
    }
    // ユーザーのコメント（返信）を取得するエンドポイント
    @GetMapping("/{username}/replies")
    public ResponseEntity<List<Reply>> getRepliesByUser(
            @PathVariable String username) {
        var replies = replyService.getRepliesByUser(username);
        return ResponseEntity.ok(replies);
    }

    // ユーザーがいいねしたユーザー一覧を取得するエンドポイント
    @GetMapping("/{username}/liked-users")
    public ResponseEntity<List<LikedUser>> getLikedUserByUser(
            @PathVariable String username,
            Authentication authentication) {
        var likedUsers = userService.getLikedUsersByUser(
                username,
                (UserEntity) authentication.getPrincipal()
        );
        return ResponseEntity.ok(likedUsers);
    }

    @PostMapping
    public ResponseEntity<User> signUp(
            @Valid @RequestBody
            UserSignUpRequestBody userSignUpRequestBody) {
        var user =
                userService.signUp(userSignUpRequestBody.username(), userSignUpRequestBody.password());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserAuthenticationResponse> authenticate(
            @Valid @RequestBody
            UserLoginRequestBody userLoginRequestBody) {
        var response =
                userService.authenticate(userLoginRequestBody.username(), userLoginRequestBody.password());
        return ResponseEntity.ok(response);
    }
}
