package com.example.app.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain model for audit logging.
 * Tracks all important actions in the system.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
    
    private UUID id;
    private UUID userId;
    private String username;
    private String action; // CREATE, UPDATE, DELETE, LOGIN, LOGOUT, etc.
    private String entityName;
    private String entityId;
    private String requestMethod; // GET, POST, PUT, DELETE
    private String requestUrl;
    private String ipAddress;
    private String userAgent;
    private boolean success;
    private String errorMessage;
    private LocalDateTime timestamp;
    
    /**
     * Business logic: Check if action was successful
     */
    public boolean wasSuccessful() {
        return success;
    }
    
    /**
     * Business logic: Check if this is a security-related action
     */
    public boolean isSecurityAction() {
        return action != null && (
            action.equals("LOGIN") || 
            action.equals("LOGOUT") || 
            action.equals("REGISTER") ||
            action.equals("PASSWORD_CHANGE") ||
            action.equals("TOKEN_REFRESH")
        );
    }
    
    /**
     * Business logic: Check if this is a data modification action
     */
    public boolean isDataModification() {
        return action != null && (
            action.equals("CREATE") || 
            action.equals("UPDATE") || 
            action.equals("DELETE")
        );
    }
}
