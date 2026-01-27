package com.example.app.adapter.web.request;

import com.example.app.domain.valueobject.BedType;
import com.example.app.domain.valueobject.RoomStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Request DTO for updating an existing room.
 * All fields are optional (partial updates supported).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoomRequest {
    
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;
    
    private String description;
    
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal pricePerNight;
    
    @Min(value = 0, message = "Rating must be at least 0")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Double rating;
    
    @Min(value = 1, message = "Max guests must be at least 1")
    private Integer maxGuests;
    
    private BedType bedType;
    
    @Min(value = 0, message = "Room size must be positive")
    private Double roomSize;
    
    private List<String> tags;
    
    private List<String> amenities;
    
    private List<String> badges;
    
    private RoomStatus status;
}
