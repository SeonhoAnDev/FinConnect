package com.example.finconnect.model.user;

import java.time.Instant;

public record LikedUser(
        Long userId,
        String username,
        String profile,
        String description,
        Long followersCount,
        Long followingsCount,
        Instant createddatetime,
        Instant updateddatetime,
        Instant deleteddatetime,
        Boolean isFollowing,
        Long likedPostId,
        Instant likeddatetime) {

    public static LikedUser from(User user, Long likedPostId, Instant likeddatetime) {
        return new LikedUser(
                user.userId(),
                user.username(),
                user.profile(),
                user.description(),
                user.followersCount(),
                user.followingsCount(),
                user.createddatetime(),
                user.updateddatetime(),
                user.deleteddatetime(),
                user.isFollowing(),
                likedPostId,
                likeddatetime);
    }
}
