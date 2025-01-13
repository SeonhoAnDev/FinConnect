package com.example.finconnect.model;

import java.time.ZonedDateTime;
import java.util.Objects;

public class Post {
    private Long postId;

    private String body;

    private ZonedDateTime createdAt;

    public Post(Long postId, String body, ZonedDateTime createdAt) {
        this.postId = postId;
        this.body = body;
        this.createdAt = createdAt;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(postId, post.postId) && Objects.equals(body, post.body) && Objects.equals(createdAt, post.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, body, createdAt);
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", body='" + body + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
