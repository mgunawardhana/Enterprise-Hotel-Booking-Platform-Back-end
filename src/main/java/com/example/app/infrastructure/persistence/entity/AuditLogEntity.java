package com.example.app.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity for Audit Log table
 */
@Entity
@Table(name = "audit_logs", indexes = {
    @Index(name = "idx_audit_user_id", columnList = "user_id"),
    @Index(name = "idx_audit_action", columnList = "action"),
    @Index(name = "idx_audit_timestamp", columnList = "timestamp")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "user_id")
    private UUID userId;
    
    @Column(name = "username", length = 50)
    private String username;
    
    @Column(name = "action", nullable = false, length = 50)
    private String action;
    
    @Column(name = "entity_name", length = 100)
    private String entityName;
    
    @Column(name = "entity_id", length = 100)
    private String entityId;
    
    @Column(name = "request_method", length = 10)
    private String requestMethod;
    
    @Column(name = "request_url", length = 500)
    private String requestUrl;
    
    @Column(name = "ip_address", length = 45)
    private String ipAddress;
    
    @Column(name = "user_agent", length = 500)
    private String userAgent;
    
    @Column(name = "success", nullable = false)
    private boolean success;
    
    @Column(name = "error_message", length = 1000)
    private String errorMessage;
    
    @CreationTimestamp
    @Column(name = "timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp;
}
