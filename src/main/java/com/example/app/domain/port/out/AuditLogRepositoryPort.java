package com.example.app.domain.port.out;

import com.example.app.domain.model.AuditLog;

/**
 * Output port (repository interface) for AuditLog persistence.
 */
public interface AuditLogRepositoryPort {
    
    /**
     * Save audit log entry
     */
    AuditLog save(AuditLog auditLog);
}
