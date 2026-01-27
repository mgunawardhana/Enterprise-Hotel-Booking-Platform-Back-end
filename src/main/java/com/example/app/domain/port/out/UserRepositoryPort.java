package com.example.app.domain.port.out;

import com.example.app.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port (repository interface) for User persistence.
 * Defines what the domain needs from the infrastructure layer.
 */
public interface UserRepositoryPort {
    
    /**
     * Save or update user
     */
    User save(User user);
    
    /**
     * Find user by ID
     */
    Optional<User> findById(UUID id);
    
    /**
     * Find user by username
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find user by provider and provider ID (for OAuth)
     */
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
    
    /**
     * Check if username exists
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Find all users with pagination
     */
    List<User> findAll(int page, int size);
    
    /**
     * Count all users
     */
    long count();
    
    /**
     * Delete user
     */
    void delete(UUID id);
}
