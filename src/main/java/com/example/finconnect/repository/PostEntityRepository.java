package com.example.finconnect.repository;

import com.example.finconnect.model.entity.PostEntity;
import com.example.finconnect.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostEntityRepository extends JpaRepository<PostEntity, Long> {
    List<PostEntity> findByUser(UserEntity user);
    List<PostEntity> findByBodyContainingIgnoreCase(String keyword);
}
