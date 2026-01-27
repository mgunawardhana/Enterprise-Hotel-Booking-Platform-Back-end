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
 * Request DTO for creating a new room.
 * Includes Jakarta validation annotations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomRequest {
    
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotNull(message = "Price per night is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal pricePerNight;
    
    @Min(value = 0, message = "Rating must be at least 0")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Double rating;
    
    @NotNull(message = "Max guests is required")
    @Min(value = 1, message = "Max guests must be at least 1")
    private Integer maxGuests;
    
    @NotNull(message = "Bed type is required")
    private BedType bedType;
    
    @Min(value = 0, message = "Room size must be positive")
    private Double roomSize;
    
    private List<String> tags;
    
    private List<String> amenities;
    
    private List<String> badges;
    
    @NotNull(message = "Status is required")
    private RoomStatus status;
}
