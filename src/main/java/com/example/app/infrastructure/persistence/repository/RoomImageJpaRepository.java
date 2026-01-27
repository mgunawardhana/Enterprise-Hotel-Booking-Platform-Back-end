package com.example.app.infrastructure.persistence.repository;

import com.example.app.infrastructure.persistence.entity.RoomImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for RoomImageEntity.
 */
@Repository
public interface RoomImageJpaRepository extends JpaRepository<RoomImageEntity, UUID> {
    
    /**
     * Find all images for a specific room
     */
    @Query("SELECT ri FROM RoomImageEntity ri WHERE ri.room.id = :roomId ORDER BY ri.displayOrder ASC")
    List<RoomImageEntity> findByRoomId(@Param("roomId") UUID roomId);
    
    /**
     * Find the main image for a room
     */
    @Query("SELECT ri FROM RoomImageEntity ri WHERE ri.room.id = :roomId AND ri.isMain = true")
    Optional<RoomImageEntity> findByRoomIdAndIsMainTrue(@Param("roomId") UUID roomId);
    
    /**
     * Delete all images for a room
     */
    void deleteByRoomId(UUID roomId);
}
