package com.example.app.infrastructure.persistence.adapter;

import com.example.app.domain.model.AuditLog;
import com.example.app.domain.port.out.AuditLogRepositoryPort;
import com.example.app.infrastructure.persistence.entity.AuditLogEntity;
import com.example.app.infrastructure.persistence.repository.AuditLogJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Adapter that implements AuditLogRepositoryPort using JPA
 */
@Component
@RequiredArgsConstructor
public class AuditLogRepositoryAdapter implements AuditLogRepositoryPort {
    
    private final AuditLogJpaRepository auditLogJpaRepository;
    
    @Override
    public AuditLog save(AuditLog auditLog) {
        AuditLogEntity entity = toEntity(auditLog);
        AuditLogEntity saved = auditLogJpaRepository.save(entity);
        return toDomain(saved);
    }
    
    private AuditLogEntity toEntity(AuditLog auditLog) {
        return AuditLogEntity.builder()
                .id(auditLog.getId())
                .userId(auditLog.getUserId())
                .username(auditLog.getUsername())
                .action(auditLog.getAction())
                .entityName(auditLog.getEntityName())
                .entityId(auditLog.getEntityId())
                .requestMethod(auditLog.getRequestMethod())
                .requestUrl(auditLog.getRequestUrl())
                .ipAddress(auditLog.getIpAddress())
                .userAgent(auditLog.getUserAgent())
                .success(auditLog.isSuccess())
                .errorMessage(auditLog.getErrorMessage())
                .timestamp(auditLog.getTimestamp())
                .build();
    }
    
    private AuditLog toDomain(AuditLogEntity entity) {
        return AuditLog.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .username(entity.getUsername())
                .action(entity.getAction())
                .entityName(entity.getEntityName())
                .entityId(entity.getEntityId())
                .requestMethod(entity.getRequestMethod())
                .requestUrl(entity.getRequestUrl())
                .ipAddress(entity.getIpAddress())
                .userAgent(entity.getUserAgent())
                .success(entity.isSuccess())
                .errorMessage(entity.getErrorMessage())
                .timestamp(entity.getTimestamp())
                .build();
    }
}
