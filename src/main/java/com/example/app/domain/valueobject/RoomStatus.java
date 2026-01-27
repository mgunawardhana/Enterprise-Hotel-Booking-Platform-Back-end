package com.example.app.domain.valueobject;

/**
 * Enum representing room availability status.
 * Part of the domain layer - no framework dependencies.
 */
public enum RoomStatus {
    AVAILABLE("Available for booking"),
    OCCUPIED("Currently occupied"),
    MAINTENANCE("Under maintenance"),
    RESERVED("Reserved");
    
    private final String description;
    
    RoomStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
