package com.example.app.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity for Room Images stored in PostgreSQL.
 * Uses BYTEA (byte array) for storing image binary data.
 * Lazy loading is used for imageData to optimize performance.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room_images", indexes = {
    @Index(name = "idx_room_image_room_id", columnList = "room_id"),
    @Index(name = "idx_room_image_is_main", columnList = "is_main")
})
public class RoomImageEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private RoomEntity room;
    
    @Column(name = "filename", nullable = false)
    private String filename;
    
    @Column(name = "content_type", nullable = false)
    private String contentType;
    
    @Column(name = "file_size", nullable = false)
    private Long fileSize;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "image_data", nullable = false, columnDefinition = "BYTEA")
    private byte[] imageData;
    
    @Column(name = "is_main", nullable = false)
    private boolean isMain = false;
    
    @Column(name = "display_order")
    private Integer displayOrder = 0;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
