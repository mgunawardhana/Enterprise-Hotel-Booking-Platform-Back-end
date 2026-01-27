package com.example.app.domain.valueobject;

/**
 * Enum representing different bed types available in rooms.
 * Part of the domain layer - no framework dependencies.
 */
public enum BedType {
    SINGLE("Single Bed"),
    DOUBLE("Double Bed"),
    QUEEN("Queen Bed"),
    KING("King Bed"),
    TWIN("Twin Beds");
    
    private final String displayName;
    
    BedType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
