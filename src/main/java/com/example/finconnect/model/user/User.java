package com.example.finconnect.model.user;

import com.example.finconnect.model.entity.UserEntity;

import java.time.Instant;

public record User(
        Long userId,
        String username,
        String profile,
        String description,
        Long followersCount,
        Long followingsCount,
        Instant createddatetime,
        Instant updateddatetime,
        Instant deleteddatetime,
        Boolean isFollowing) {

    public static User from(Long userId, String username, String profile, String description,
                            Long followersCount, Long followingsCount, Instant createdDateTime,
                            Instant updatedDateTime, Boolean isFollowing) {
        return new User(userId, username, profile, description, followersCount, followingsCount,
                createdDateTime, updatedDateTime, null, isFollowing);
    }

    public static User from(UserEntity user) {
        return new User(
                user.getUserId(),
                user.getUsername(),
                user.getProfile(),
                user.getDescription(),
                user.getFollowersCount(),
                user.getFollowingsCount(),
                user.getCreateddatetime().toInstant(),
                user.getUpdateddatetime().toInstant(),
                user.getDeleteddatetime() != null ? user.getDeleteddatetime().toInstant() : null,
                null);
    }

    public static User from(UserEntity user, boolean isFollowing) {
        return new User(
                user.getUserId(),
                user.getUsername(),
                user.getProfile(),
                user.getDescription(),
                user.getFollowersCount(),
                user.getFollowingsCount(),
                user.getCreateddatetime().toInstant(),
                user.getUpdateddatetime().toInstant(),
                user.getDeleteddatetime() != null ? user.getDeleteddatetime().toInstant() : null,
                isFollowing);
    }
}
