package com.example.app.adapter.web.response;

import com.example.app.domain.valueobject.BedType;
import com.example.app.domain.valueobject.RoomView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for room search results.
 * Optimized for listing view with minimal data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomSearchResponse {
    
    private UUID id;
    private String title;
    private String description;
    private BigDecimal pricePerNight;
    private Double rating;
    private Integer maxGuests;
    private BedType bedType;
    private Double roomSize;
    private List<String> amenities;
    private RoomView view;
    private String mainImage; // Only one image for performance
    private List<String> badges;
}
