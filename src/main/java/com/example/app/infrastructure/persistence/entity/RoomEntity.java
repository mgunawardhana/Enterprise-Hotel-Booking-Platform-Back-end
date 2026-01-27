package com.example.app.infrastructure.persistence.entity;

import com.example.app.domain.valueobject.BedType;
import com.example.app.domain.valueobject.RoomStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Entity for Room table.
 * Extends BaseEntity for common audit fields.
 * Includes performance optimizations: indexes, lazy loading, batch fetching.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "rooms", indexes = {
    @Index(name = "idx_room_price", columnList = "price_per_night"),
    @Index(name = "idx_room_rating", columnList = "rating"),
    @Index(name = "idx_room_status", columnList = "status"),
    @Index(name = "idx_room_deleted", columnList = "is_deleted")
})
public class RoomEntity extends BaseEntity {
    
    @Column(name = "title", nullable = false, length = 200)
    private String title;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "price_per_night", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;
    
    @Column(name = "rating")
    private Double rating;
    
    @Column(name = "max_guests", nullable = false)
    private Integer maxGuests;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "bed_type", nullable = false, length = 20)
    private BedType bedType;
    
    @Column(name = "room_size")
    private Double roomSize;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "room_tags", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "room_amenities", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "amenity")
    private List<String> amenities = new ArrayList<>();
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "room_badges", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "badge")
    private List<String> badges = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private RoomStatus status;
    
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RoomImageEntity> images = new ArrayList<>();
}
