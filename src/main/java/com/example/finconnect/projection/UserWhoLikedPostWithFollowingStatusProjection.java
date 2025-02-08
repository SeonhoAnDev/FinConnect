package com.example.finconnect.projection;

import java.time.Instant;

public interface UserWhoLikedPostWithFollowingStatusProjection {
    Long getUserId();
    String getUsername();
    String getProfile();
    String getDescription();
    Long getFollowersCount();
    Long getFollowingsCount();
    Instant getCreatedDateTime();
    Instant getUpdatedDateTime();
    Instant getLikedDateTime();
    Long getLikedPostId();
    Boolean getIsFollowing();
} 