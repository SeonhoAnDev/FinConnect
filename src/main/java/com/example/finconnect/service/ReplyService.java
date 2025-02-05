package com.example.finconnect.service;

import com.example.finconnect.exception.post.PostNotFoundException;
import com.example.finconnect.exception.reply.ReplyNotFoundException;
import com.example.finconnect.exception.user.UserNotAllowedException;
import com.example.finconnect.exception.user.UserNotFoundException;
import com.example.finconnect.model.entity.ReplyEntity;
import com.example.finconnect.model.entity.UserEntity;
import com.example.finconnect.model.post.Post;
import com.example.finconnect.model.post.PostPatchRequestBody;
import com.example.finconnect.model.reply.Reply;
import com.example.finconnect.model.reply.ReplyPatchRequestBody;
import com.example.finconnect.model.reply.ReplyPostRequestBody;
import com.example.finconnect.repository.PostEntityRepository;
import com.example.finconnect.repository.ReplyEntityRepository;
import com.example.finconnect.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReplyService {
    private static final List<Reply> reply = new ArrayList<>();
    @Autowired
    private ReplyEntityRepository replyEntityRepository;
    @Autowired
    private PostEntityRepository postEntityRepository;
    @Autowired
    private UserEntityRepository userEntityRepository;

    public Page<Reply> getRepliesByPostId(Long postId, Pageable pageable) {
        var postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        var replyEntities = replyEntityRepository.findByPost(postEntity, pageable);
        return replyEntities.map(Reply::from);
    }

    @Transactional
    public Reply creatReply(Long postId, ReplyPostRequestBody replyPostRequestBody, UserEntity currentUser) {
        var postEntity =
                postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        var replyEntity =
                replyEntityRepository.save(
                        ReplyEntity.of(replyPostRequestBody.body(), currentUser, postEntity));

        postEntity.setRepliesCount(postEntity.getRepliesCount() + 1);

        return Reply.from(replyEntity);
    }

    @Transactional
    public Reply updateReply(
            Long postId,
            Long replyId,
            ReplyPatchRequestBody replypatchRequestBody,
            UserEntity currentUser) {
        postEntityRepository
                .findById(postId)
                .orElseThrow(
                        () -> new PostNotFoundException(postId));
        var replyEntity = replyEntityRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFoundException(replyId));
        if (!replyEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }
        replyEntity.setBody(replypatchRequestBody.body());
        return Reply.from(replyEntityRepository.save(replyEntity));
    }

    @Transactional
    public void deleteReply(Long postId, Long replyId, UserEntity currentUser) {
        var postEntity =  postEntityRepository
                .findById(postId)
                .orElseThrow(
                        () -> new PostNotFoundException(postId));
        var replyEntity =
                replyEntityRepository
                        .findById(replyId)
                        .orElseThrow(
                                () -> new PostNotFoundException(replyId));

        if (!replyEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        replyEntityRepository.delete(replyEntity);

        postEntity.setRepliesCount(Math.max(0,postEntity.getRepliesCount() - 1));
        postEntityRepository.save(postEntity);
    }

    public List<Reply> getRepliesByUser(String username) {
        var userEntity =
                userEntityRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new UserNotFoundException(username));
        var replyEntities = replyEntityRepository.findByUser(userEntity);
        return replyEntities.stream().map(Reply::from).toList();
    }
}
