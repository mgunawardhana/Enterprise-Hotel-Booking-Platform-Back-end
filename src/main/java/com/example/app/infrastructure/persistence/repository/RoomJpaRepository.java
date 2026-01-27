package com.example.app.infrastructure.persistence.repository;

import com.example.app.infrastructure.persistence.entity.RoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for RoomEntity.
 * Includes custom queries for soft delete support and performance optimization.
 */
@Repository
public interface RoomJpaRepository extends JpaRepository<RoomEntity, UUID> {
    
    /**
     * Find all non-deleted rooms with pagination
     * Uses custom query to ensure soft-deleted rooms are excluded
     */
    @Query("SELECT r FROM RoomEntity r WHERE r.deleted = false")
    Page<RoomEntity> findAllByDeletedFalse(Pageable pageable);
    
    /**
     * Find a single non-deleted room by ID
     */
    @Query("SELECT r FROM RoomEntity r WHERE r.id = :id AND r.deleted = false")
    Optional<RoomEntity> findByIdAndDeletedFalse(@Param("id") UUID id);
    
    /**
     * Check if a non-deleted room exists by ID
     */
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM RoomEntity r WHERE r.id = :id AND r.deleted = false")
    boolean existsByIdAndDeletedFalse(@Param("id") UUID id);
}
