package com.example.app.domain.model;

import com.example.app.domain.valueobject.BedType;
import com.example.app.domain.valueobject.RoomStatus;
import com.example.app.domain.valueobject.RoomView;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Domain model representing a hotel room.
 * Pure domain object with NO Spring/JPA annotations.
 * This ensures the domain layer remains framework-independent.
 */
public class Room {
    
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
    private RoomView view;
    private RoomStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID createdBy;
    private UUID updatedBy;
    private boolean deleted;
    
    // Default constructor
    public Room() {
        this.status = RoomStatus.AVAILABLE;
        this.deleted = false;
    }
    
    // Full constructor
    public Room(UUID id, String title, String description, BigDecimal pricePerNight,
                Double rating, Integer maxGuests, BedType bedType, Double roomSize,
                List<String> tags, List<String> amenities, List<String> badges, RoomView view,
                RoomStatus status, LocalDateTime createdAt, LocalDateTime updatedAt,
                UUID createdBy, UUID updatedBy, boolean deleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.pricePerNight = pricePerNight;
        this.rating = rating;
        this.maxGuests = maxGuests;
        this.bedType = bedType;
        this.roomSize = roomSize;
        this.tags = tags;
        this.amenities = amenities;
        this.badges = badges;
        this.view = view;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.deleted = deleted;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }
    
    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }
    
    public Double getRating() {
        return rating;
    }
    
    public void setRating(Double rating) {
        this.rating = rating;
    }
    
    public Integer getMaxGuests() {
        return maxGuests;
    }
    
    public void setMaxGuests(Integer maxGuests) {
        this.maxGuests = maxGuests;
    }
    
    public BedType getBedType() {
        return bedType;
    }
    
    public void setBedType(BedType bedType) {
        this.bedType = bedType;
    }
    
    public Double getRoomSize() {
        return roomSize;
    }
    
    public void setRoomSize(Double roomSize) {
        this.roomSize = roomSize;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    
    public List<String> getAmenities() {
        return amenities;
    }
    
    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }
    
    public List<String> getBadges() {
        return badges;
    }
    
    public void setBadges(List<String> badges) {
        this.badges = badges;
    }
    
    public RoomView getView() {
        return view;
    }
    
    public void setView(RoomView view) {
        this.view = view;
    }
    
    public RoomStatus getStatus() {
        return status;
    }
    
    public void setStatus(RoomStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public UUID getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }
    
    public UUID getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(UUID updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public boolean isDeleted() {
        return deleted;
    }
    
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
