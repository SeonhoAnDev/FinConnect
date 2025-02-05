package com.example.finconnect.controller;

import com.example.finconnect.model.entity.UserEntity;
import com.example.finconnect.model.reply.Reply;
import com.example.finconnect.model.reply.ReplyPatchRequestBody;
import com.example.finconnect.model.reply.ReplyPostRequestBody;
import com.example.finconnect.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/posts/{postId}/replies")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getRepliesByPostId(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page) {
        final int PAGE_SIZE = 20;
        var pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("createdDateTime").descending());
        
        var replyPage = replyService.getRepliesByPostId(postId, pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("content", replyPage.getContent());
        response.put("totalPages", replyPage.getTotalPages());
        response.put("hasNext", replyPage.hasNext());
        
        return ResponseEntity.ok(response);
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
