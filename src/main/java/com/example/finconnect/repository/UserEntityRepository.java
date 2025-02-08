package com.example.finconnect.repository;

import com.example.finconnect.model.entity.UserEntity;
import com.example.finconnect.projection.UserWhoLikedPostWithFollowingStatusProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    @Query("SELECT u FROM UserEntity u WHERE u.username LIKE %:username%")
    List<UserEntity> findByUsernameContaining(@Param("username") String username);

    @Query("SELECT u FROM UserEntity u WHERE u.username LIKE %:username%")
    Page<UserEntity> findByUsernameContaining(@Param("username") String username, Pageable pageable);

    @Query(value = """
        SELECT
            u.userid AS userId,
            u.username AS username,
            u.profile AS profile,
            u.description AS description,
            u.followerscount AS followersCount,
            u.followingscount AS followingsCount,
            u.createddatetime AS createdDateTime,
            u.updateddatetime AS updatedDateTime,
            l.createddatetime AS likedDateTime,
            l.postid AS likedPostId,
            (CASE WHEN f.followid IS NOT NULL THEN TRUE ELSE FALSE END) AS isFollowing
        FROM
            "user" u
        INNER JOIN
            "like" l ON u.userid = l.userid
        INNER JOIN
            post p ON l.postid = p.postid
        LEFT JOIN
            "follow" f ON u.userid = f.following AND f.follower = :currentUserId
        WHERE
            p.userid = :userId
        """, nativeQuery = true)
    List<UserWhoLikedPostWithFollowingStatusProjection> findUsersWhoLikedPostByUserIdWithFollowingStatus(
        @Param("userId") Long userId,
        @Param("currentUserId") Long currentUserId
    );
}
