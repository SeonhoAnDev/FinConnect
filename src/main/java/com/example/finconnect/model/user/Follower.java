package com.example.finconnect.model.user;

import com.example.finconnect.model.entity.UserEntity;

import java.time.ZonedDateTime;

public record Follower(
        Long userId,
        String username,
        String profile,
        String description,
        Long followersCount,
        Long followingsCount,
        ZonedDateTime createddatetime,
        ZonedDateTime updateddatetime,
        ZonedDateTime deleteddatetime,
        ZonedDateTime followeddatetime,
        Boolean isFollowing) {
    public static Follower from(User user, ZonedDateTime followeddatetime) {
        return new Follower(
                user.userId(),
                user.username(),
                user.profile(),
                user.description(),
                user.followersCount(),
                user.followingsCount(),
                user.createddatetime(),
                user.updateddatetime(),
                user.deleteddatetime(),
                followeddatetime,
                user.isFollowing());
    }
}
