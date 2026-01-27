package com.example.app.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain model representing a room image.
 * Pure domain object with NO Spring/JPA annotations.
 */
public class RoomImage {
    
    private UUID id;
    private UUID roomId;
    private String filename;
    private String contentType;
    private Long fileSize;
    private byte[] imageData;
    private boolean isMain;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Default constructor
    public RoomImage() {
        this.isMain = false;
        this.displayOrder = 0;
    }
    
    // Full constructor
    public RoomImage(UUID id, UUID roomId, String filename, String contentType,
                     Long fileSize, byte[] imageData, boolean isMain,
                     Integer displayOrder, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.roomId = roomId;
        this.filename = filename;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.imageData = imageData;
        this.isMain = isMain;
        this.displayOrder = displayOrder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public UUID getRoomId() {
        return roomId;
    }
    
    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }
    
    public String getFilename() {
        return filename;
    }
    
    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    
    public byte[] getImageData() {
        return imageData;
    }
    
    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
    
    public boolean isMain() {
        return isMain;
    }
    
    public void setMain(boolean main) {
        isMain = main;
    }
    
    public Integer getDisplayOrder() {
        return displayOrder;
    }
    
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
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
}
