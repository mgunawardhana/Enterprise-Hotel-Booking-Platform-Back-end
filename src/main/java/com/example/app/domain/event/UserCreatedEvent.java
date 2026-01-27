package com.example.app.domain.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain event fired when a new user is created.
 * Can be used for async processing like sending welcome emails.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedEvent {
    
    private UUID userId;
    private String username;
    private String email;
    private LocalDateTime timestamp;
    private String provider; // LOCAL or GOOGLE
    
    public UserCreatedEvent(UUID userId, String username, String email, String provider) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.provider = provider;
        this.timestamp = LocalDateTime.now();
    }
}
