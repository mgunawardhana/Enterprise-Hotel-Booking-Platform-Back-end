package com.example.app.domain.valueobject;

/**
 * Enum representing different room views.
 * Part of the domain layer - no framework dependencies.
 */
public enum RoomView {
    OCEAN_VIEW("Ocean View"),
    GARDEN_VIEW("Garden View"),
    PANORAMIC_VIEW("Panoramic View");
    
    private final String displayName;
    
    RoomView(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
