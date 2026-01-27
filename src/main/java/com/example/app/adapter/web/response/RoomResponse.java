package com.example.app.adapter.web.response;

import com.example.app.domain.valueobject.BedType;
import com.example.app.domain.valueobject.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for room list views.
 * Lightweight version without image data for performance.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    
    private UUID id;
    private String title;
    private String description;
    private BigDecimal pricePerNight;
    private Double rating;
    private Integer maxGuests;
    private BedType bedType;
    private Double roomSize;
    private List<String> tags;
    private List<String> amenities;
    private List<String> badges;
    private RoomStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
