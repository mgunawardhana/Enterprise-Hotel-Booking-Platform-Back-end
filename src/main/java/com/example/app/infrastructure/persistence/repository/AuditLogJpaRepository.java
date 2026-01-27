package com.example.app.infrastructure.persistence.repository;

import com.example.app.infrastructure.persistence.entity.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for AuditLogEntity
 */
@Repository
public interface AuditLogJpaRepository extends JpaRepository<AuditLogEntity, UUID> {
    // Additional query methods can be added here as needed
}
