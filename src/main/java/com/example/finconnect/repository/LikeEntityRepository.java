package com.example.finconnect.repository;

import com.example.finconnect.model.entity.LikeEntity;
import com.example.finconnect.model.entity.PostEntity;
import com.example.finconnect.model.entity.UserEntity;
import com.example.finconnect.model.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeEntityRepository extends JpaRepository<LikeEntity, Long> {
    List<LikeEntity> findByUser(UserEntity user);
    List<LikeEntity> findByPost(PostEntity post);
    Optional<LikeEntity> findByUserAndPost(UserEntity user, PostEntity post);
}
