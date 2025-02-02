package com.example.finconnect.model.user;

import com.example.finconnect.model.entity.UserEntity;

import java.time.ZonedDateTime;

public record User(
        Long userId,
        String username,
        String profile,
        String description,
        Long followersCount,
        Long followingsCount,
        ZonedDateTime createddatetime,
        ZonedDateTime updateddatetime,
        ZonedDateTime deleteddatetime,
        Boolean isFollowing) {
    public static User from(UserEntity user) {
        return new User(
                user.getUserId(),
                user.getUsername(),
                user.getProfile(),
                user.getDescription(),
                user.getFollowersCount(),
                user.getFollowingsCount(),
                user.getCreateddatetime(),
                user.getUpdateddatetime(),
                user.getDeleteddatetime(),
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
                user.getCreateddatetime(),
                user.getUpdateddatetime(),
                user.getDeleteddatetime(),
                isFollowing);
    }
}
