package com.example.app.adapter.web.response;

import com.example.app.domain.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Response DTO for User
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    
    private UUID id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String profilePicture;
    private boolean enabled;
    private Set<Role> roles;
    private String provider;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
