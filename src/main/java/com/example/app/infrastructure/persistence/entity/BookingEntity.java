package com.example.app.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

/**
 * JPA Entity for Booking table.
 * Used for availability checking in room search.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings", indexes = {
    @Index(name = "idx_booking_room_id", columnList = "room_id"),
    @Index(name = "idx_booking_dates", columnList = "check_in_date, check_out_date"),
    @Index(name = "idx_booking_status", columnList = "status")
})
public class BookingEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "room_id", nullable = false)
    private UUID roomId;
    
    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;
    
    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;
    
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private java.time.LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
        updatedAt = java.time.LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = java.time.LocalDateTime.now();
    }
}
