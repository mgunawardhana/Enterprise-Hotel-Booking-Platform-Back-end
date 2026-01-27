package com.example.app.domain.model;

import com.example.app.domain.valueobject.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Domain model representing a User.
 * This is pure business logic with NO Spring dependencies.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    private UUID id;
    private String username;
    private Email email;
    private String password;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
    
    private String provider; // LOCAL, GOOGLE
    private String providerId;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID createdBy;
    private UUID updatedBy;
    private boolean deleted;
    
    /**
     * Business logic: Check if user has a specific role
     */
    public boolean hasRole(String roleName) {
        return roles.stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }
    
    /**
     * Business logic: Check if user is admin
     */
    public boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }
    
    /**
     * Business logic: Add role to user
     */
    public void addRole(Role role) {
        this.roles.add(role);
    }
    
    /**
     * Business logic: Remove role from user
     */
    public void removeRole(Role role) {
        this.roles.remove(role);
    }
    
    /**
     * Business logic: Get full name
     */
    public String getFullName() {
        if (firstName == null && lastName == null) {
            return username;
        }
        return String.format("%s %s", 
            firstName != null ? firstName : "", 
            lastName != null ? lastName : "").trim();
    }
    
    /**
     * Business logic: Check if OAuth user
     */
    public boolean isOAuthUser() {
        return provider != null && !provider.equals("LOCAL");
    }
    
    /**
     * Business logic: Validate user state
     */
    public boolean isAccountValid() {
        return enabled && accountNonExpired && accountNonLocked && credentialsNonExpired && !deleted;
    }
}
