package com.example.app.adapter.web.request;

import com.example.app.domain.valueobject.BedType;
import com.example.app.domain.valueobject.RoomView;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Request DTO for room search endpoint.
 * All fields are optional to support flexible filtering.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomSearchRequest {
    
    @Min(value = 0, message = "minPrice must be non-negative")
    private BigDecimal minPrice;
    
    @Min(value = 0, message = "maxPrice must be non-negative")
    private BigDecimal maxPrice;
    
    @Min(value = 1, message = "minGuests must be at least 1")
    private Integer minGuests;
    
    private List<BedType> bedTypes;
    
    private List<String> amenities;
    
    private List<RoomView> views;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate checkIn;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate checkOut;
    
    @Builder.Default
    private String sortBy = "price";
    
    @Builder.Default
    private String sortDirection = "ASC";
    
    @Min(value = 0, message = "page must be non-negative")
    @Builder.Default
    private Integer page = 0;
    
    @Min(value = 1, message = "size must be at least 1")
    @Max(value = 100, message = "size must not exceed 100")
    @Builder.Default
    private Integer size = 10;
}
