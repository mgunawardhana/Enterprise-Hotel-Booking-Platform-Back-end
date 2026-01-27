package com.example.app.domain.port.in;

import com.example.app.domain.model.User;

import java.util.List;
import java.util.UUID;

/**
 * Input port (use case interface) for user management operations.
 * Defines what the application can do regarding user management.
 */
public interface UserManagementUseCase {
    
    /**
     * Get user by ID
     */
    User getUserById(UUID userId);
    
    /**
     * Get user by username
     */
    User getUserByUsername(String username);
    
    /**
     * Get user by email
     */
    User getUserByEmail(String email);
    
    /**
     * Get all users with pagination
     */
    List<User> getAllUsers(int page, int size);
    
    /**
     * Update user information
     */
    User updateUser(UUID userId, String firstName, String lastName, String email);
    
    /**
     * Delete user (soft delete)
     */
    void deleteUser(UUID userId);
    
    /**
     * Enable or disable user account
     */
    User toggleUserStatus(UUID userId, boolean enabled);
    
    /**
     * Assign role to user
     */
    User assignRole(UUID userId, String roleName);
    
    /**
     * Remove role from user
     */
    User removeRole(UUID userId, String roleName);
    
    /**
     * Count total users
     */
    long countUsers();
}
