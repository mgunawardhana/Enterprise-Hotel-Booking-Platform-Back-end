package com.example.app.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain model representing a Role.
 * Pure business logic with NO Spring dependencies.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    
    private UUID id;
    private String name; // ROLE_ADMIN, ROLE_USER
    private String description;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deleted;
    
    /**
     * Business logic: Check if this is admin role
     */
    public boolean isAdminRole() {
        return "ROLE_ADMIN".equals(name);
    }
    
    /**
     * Business logic: Check if this is user role
     */
    public boolean isUserRole() {
        return "ROLE_USER".equals(name);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return name != null && name.equals(role.name);
    }
    
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
