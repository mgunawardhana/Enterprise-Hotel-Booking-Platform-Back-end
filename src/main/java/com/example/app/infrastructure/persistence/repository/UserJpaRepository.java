package com.example.app.infrastructure.persistence.repository;

import com.example.app.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for UserEntity
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
    
    Optional<UserEntity> findByUsername(String username);
    
    Optional<UserEntity> findByEmail(String email);
    
    Optional<UserEntity> findByProviderAndProviderId(String provider, String providerId);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM UserEntity u WHERE u.deleted = false")
    Page<UserEntity> findAllActive(Pageable pageable);
    
    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.deleted = false")
    long countActive();
}
