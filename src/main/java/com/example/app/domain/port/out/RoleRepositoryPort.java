package com.example.app.domain.port.out;

import com.example.app.domain.model.Role;

import java.util.Optional;
import java.util.UUID;

/**
 * Output port (repository interface) for Role persistence.
 */
public interface RoleRepositoryPort {
    
    /**
     * Save or update role
     */
    Role save(Role role);
    
    /**
     * Find role by ID
     */
    Optional<Role> findById(UUID id);
    
    /**
     * Find role by name
     */
    Optional<Role> findByName(String name);
    
    /**
     * Check if role exists by name
     */
    boolean existsByName(String name);
}
