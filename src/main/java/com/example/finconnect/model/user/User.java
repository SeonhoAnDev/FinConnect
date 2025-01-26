package com.example.finconnect.model.user;

import com.example.finconnect.model.entity.UserEntity;

import java.time.ZonedDateTime;

public record User(
        Long userId,
        String username,
        String profile,
        String desription,
        Long followersCount,
        Long followingsCount,
        ZonedDateTime createddatetime,
        ZonedDateTime updateddatetime,
        ZonedDateTime deleteddatetime
) {
    public static User from(UserEntity user){
        return new User(
                user.getUserId(),
                user.getUsername(),
                user.getProfile(),
                user.getDesription(),
                user.getFollowersCount(),
                user.getFollowingsCount(),
                user.getCreateddatetime(),
                user.getUpdateddatetime(),
                user.getDeleteddatetime());
    }
}
