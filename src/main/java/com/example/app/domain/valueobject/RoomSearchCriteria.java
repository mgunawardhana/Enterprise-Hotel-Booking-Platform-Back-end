package com.example.app.domain.valueobject;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Value object encapsulating all room search criteria.
 * Immutable and contains validation logic.
 */
public class RoomSearchCriteria {
    
    private final BigDecimal minPrice;
    private final BigDecimal maxPrice;
    private final Integer minGuests;
    private final List<BedType> bedTypes;
    private final List<String> amenities;
    private final List<RoomView> views;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final String sortBy;
    private final String sortDirection;
    private final Integer page;
    private final Integer size;
    
    public RoomSearchCriteria(BigDecimal minPrice, BigDecimal maxPrice, Integer minGuests,
                              List<BedType> bedTypes, List<String> amenities, List<RoomView> views,
                              LocalDate checkIn, LocalDate checkOut,
                              String sortBy, String sortDirection, Integer page, Integer size) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minGuests = minGuests;
        this.bedTypes = bedTypes;
        this.amenities = amenities;
        this.views = views;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.sortBy = sortBy != null ? sortBy : "price";
        this.sortDirection = sortDirection != null ? sortDirection : "ASC";
        this.page = page != null ? page : 0;
        this.size = size != null ? size : 10;
        
        validate();
    }
    
    /**
     * Validates the search criteria
     */
    private void validate() {
        // Validate price range
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("minPrice must be less than or equal to maxPrice");
        }
        
        // Validate price is non-negative
        if (minPrice != null && minPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("minPrice must be non-negative");
        }
        
        if (maxPrice != null && maxPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("maxPrice must be non-negative");
        }
        
        // Validate date range
        if (checkIn != null && checkOut != null && !checkIn.isBefore(checkOut)) {
            throw new IllegalArgumentException("checkIn must be before checkOut");
        }
        
        // Validate pagination
        if (page < 0) {
            throw new IllegalArgumentException("page must be non-negative");
        }
        
        if (size <= 0) {
            throw new IllegalArgumentException("size must be positive");
        }
        
        if (size > 100) {
            throw new IllegalArgumentException("size must not exceed 100");
        }
        
        // Validate sort direction
        if (!sortDirection.equalsIgnoreCase("ASC") && !sortDirection.equalsIgnoreCase("DESC")) {
            throw new IllegalArgumentException("sortDirection must be either ASC or DESC");
        }
    }
    
    // Getters
    public BigDecimal getMinPrice() {
        return minPrice;
    }
    
    public BigDecimal getMaxPrice() {
        return maxPrice;
    }
    
    public Integer getMinGuests() {
        return minGuests;
    }
    
    public List<BedType> getBedTypes() {
        return bedTypes;
    }
    
    public List<String> getAmenities() {
        return amenities;
    }
    
    public List<RoomView> getViews() {
        return views;
    }
    
    public LocalDate getCheckIn() {
        return checkIn;
    }
    
    public LocalDate getCheckOut() {
        return checkOut;
    }
    
    public String getSortBy() {
        return sortBy;
    }
    
    public String getSortDirection() {
        return sortDirection;
    }
    
    public Integer getPage() {
        return page;
    }
    
    public Integer getSize() {
        return size;
    }
}
