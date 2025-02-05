package com.example.finconnect.repository;

import com.example.finconnect.model.entity.PostEntity;
import com.example.finconnect.model.entity.ReplyEntity;
import com.example.finconnect.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyEntityRepository extends JpaRepository<ReplyEntity, Long> {
    List<ReplyEntity> findByUser(UserEntity user);

    List<ReplyEntity> findByPost(PostEntity post);

    Page<ReplyEntity> findByPost(PostEntity post, Pageable pageable);
}
