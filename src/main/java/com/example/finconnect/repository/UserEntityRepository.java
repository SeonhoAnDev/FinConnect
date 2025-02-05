package com.example.finconnect.repository;

import com.example.finconnect.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    List<UserEntity> findByUsernameContaining(String username);

    Page<UserEntity> findByUsernameContaining(String username, Pageable pageable);
}
