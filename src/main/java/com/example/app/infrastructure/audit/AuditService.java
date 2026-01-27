package com.example.app.infrastructure.audit;

import com.example.app.domain.model.AuditLog;
import com.example.app.domain.port.out.AuditLogRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service for persisting audit logs
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditService {
    
    private final AuditLogRepositoryPort auditLogRepositoryPort;
    
    /**
     * Save audit log asynchronously
     */
    @Async
    public void saveAuditLog(AuditLog auditLog) {
        try {
            auditLogRepositoryPort.save(auditLog);
            log.debug("Audit log saved: {}", auditLog.getAction());
        } catch (Exception e) {
            log.error("Failed to save audit log", e);
        }
    }
    
    /**
     * Create and save audit log
     */
    public void logAction(UUID userId, String username, String action, String entityName, 
                         String entityId, String requestMethod, String requestUrl, 
                         String ipAddress, String userAgent, boolean success, String errorMessage) {
        
        AuditLog auditLog = AuditLog.builder()
                .userId(userId)
                .username(username)
                .action(action)
                .entityName(entityName)
                .entityId(entityId)
                .requestMethod(requestMethod)
                .requestUrl(requestUrl)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .success(success)
                .errorMessage(errorMessage)
                .timestamp(LocalDateTime.now())
                .build();
        
        saveAuditLog(auditLog);
    }
}
