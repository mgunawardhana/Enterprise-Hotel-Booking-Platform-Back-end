package com.example.app.infrastructure.persistence.repository;

import com.example.app.infrastructure.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for RoleEntity
 */
@Repository
public interface RoleJpaRepository extends JpaRepository<RoleEntity, UUID> {
    
    Optional<RoleEntity> findByName(String name);
    
    boolean existsByName(String name);
}
