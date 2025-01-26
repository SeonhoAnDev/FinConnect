package com.example.finconnect.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(
        name = "\"follow\"",
        indexes = {@Index(name = "follow_follower_following_idx", columnList = "follower, following", unique = true)})
public class FollowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followId;
    @Column
    private ZonedDateTime createdDateTime;

    @ManyToOne
    @JoinColumn(name = "follower")
    private UserEntity follower;

    @ManyToOne
    @JoinColumn(name = "following")
    private UserEntity following;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowEntity that = (FollowEntity) o;
        return Objects.equals(followId, that.followId) && Objects.equals(createdDateTime, that.createdDateTime) && Objects.equals(follower, that.follower) && Objects.equals(following, that.following);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followId, createdDateTime, follower, following);
    }

    public static FollowEntity of(UserEntity follower, UserEntity following) {
        var follow = new FollowEntity();
        follow.setFollower(follower);
        follow.setFollowing(following);
        return follow;
    }

    @PrePersist
    protected void prePersist() {
        this.createdDateTime = ZonedDateTime.now();
    }
}
