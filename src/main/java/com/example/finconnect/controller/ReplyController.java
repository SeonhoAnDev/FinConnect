package com.example.finconnect.controller;

import com.example.finconnect.model.entity.UserEntity;
import com.example.finconnect.model.reply.Reply;
import com.example.finconnect.model.reply.ReplyPatchRequestBody;
import com.example.finconnect.model.reply.ReplyPostRequestBody;
import com.example.finconnect.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{postId}/replies")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @GetMapping
    public ResponseEntity<List<Reply>> getRepliesByPostId(@PathVariable Long postId) {
        var replies = replyService.getRepliesByPostId(postId);
        return ResponseEntity.ok(replies);
    }

    @PostMapping
    public ResponseEntity<Reply> creatReply(
            @PathVariable Long postId,
            @RequestBody ReplyPostRequestBody replyPostRequestBody, Authentication authentication) {
        var reply = replyService.creatReply(postId, replyPostRequestBody, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(reply);
    }

    @PatchMapping("/{replyId}")
    public ResponseEntity<Reply> updateReply(
            @PathVariable Long postId,
            @PathVariable Long replyId,
            @RequestBody ReplyPatchRequestBody replyPatchRequestBody,
            Authentication authentication) {
        var reply = replyService.updateReply(
                postId, replyId, replyPatchRequestBody, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(reply);
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<Void> deleteReply(
            @PathVariable Long postId,
            @PathVariable Long replyId,
            Authentication authentication) {
        replyService.deleteReply(
                postId, replyId,(UserEntity) authentication.getPrincipal());
        return ResponseEntity.noContent().build();
    }
}
