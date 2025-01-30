package com.example.finconnect.model.user;

import com.example.finconnect.model.entity.UserEntity;

import java.time.ZonedDateTime;

public record LikedUser(
        Long userId,
        String username,
        String profile,
        String desription,
        Long followersCount,
        Long followingsCount,
        ZonedDateTime createddatetime,
        ZonedDateTime updateddatetime,
        ZonedDateTime deleteddatetime,
        Boolean isFollowing,
        Long likedPostId,
        ZonedDateTime likeddatetime) {
    public static LikedUser from(User user, Long likedPostId, ZonedDateTime likeddatetime) {
        return new LikedUser(
                user.userId(),
                user.profile(),
                user.username(),
                user.desription(),
                user.followingsCount(),
                user.followersCount(),
                user.createddatetime(),
                user.updateddatetime(),
                user.deleteddatetime(),
                user.isFollowing(),
                likedPostId,
                likeddatetime);
    }
}
