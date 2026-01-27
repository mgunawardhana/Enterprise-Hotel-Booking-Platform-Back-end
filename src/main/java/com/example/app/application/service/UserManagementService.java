package com.example.app.application.service;

import com.example.app.common.constants.Constants;
import com.example.app.common.exception.ResourceNotFoundException;
import com.example.app.domain.model.Role;
import com.example.app.domain.model.User;
import com.example.app.domain.port.in.UserManagementUseCase;
import com.example.app.domain.port.out.RoleRepositoryPort;
import com.example.app.domain.port.out.UserRepositoryPort;
import com.example.app.domain.valueobject.Email;
import com.example.app.infrastructure.audit.Auditable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Application service implementing user management use cases
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserManagementService implements UserManagementUseCase {
    
    private final UserRepositoryPort userRepositoryPort;
    private final RoleRepositoryPort roleRepositoryPort;
    
    @Override
    public User getUserById(UUID userId) {
        return userRepositoryPort.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }
    
    @Override
    public User getUserByUsername(String username) {
        return userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }
    
    @Override
    public User getUserByEmail(String email) {
        return userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }
    
    @Override
    public List<User> getAllUsers(int page, int size) {
        log.debug("Fetching users - page: {}, size: {}", page, size);
        return userRepositoryPort.findAll(page, size);
    }
    
    @Override
    @Transactional
    @Auditable(action = Constants.ACTION_UPDATE, entityName = "User")
    public User updateUser(UUID userId, String firstName, String lastName, String email) {
        log.info("Updating user: {}", userId);
        
        User user = getUserById(userId);
        
        if (firstName != null) {
            user.setFirstName(firstName);
        }
        
        if (lastName != null) {
            user.setLastName(lastName);
        }
        
        if (email != null && !email.equals(user.getEmail().getValue())) {
            user.setEmail(new Email(email));
        }
        
        user.setUpdatedAt(LocalDateTime.now());
        
        return userRepositoryPort.save(user);
    }
    
    @Override
    @Transactional
    @Auditable(action = Constants.ACTION_DELETE, entityName = "User")
    public void deleteUser(UUID userId) {
        log.info("Deleting user: {}", userId);
        
        User user = getUserById(userId);
        userRepositoryPort.delete(userId);
    }
    
    @Override
    @Transactional
    @Auditable(action = Constants.ACTION_UPDATE, entityName = "User")
    public User toggleUserStatus(UUID userId, boolean enabled) {
        log.info("Toggling user status: {} to {}", userId, enabled);
        
        User user = getUserById(userId);
        user.setEnabled(enabled);
        user.setUpdatedAt(LocalDateTime.now());
        
        return userRepositoryPort.save(user);
    }
    
    @Override
    @Transactional
    @Auditable(action = Constants.ACTION_UPDATE, entityName = "User")
    public User assignRole(UUID userId, String roleName) {
        log.info("Assigning role {} to user {}", roleName, userId);
        
        User user = getUserById(userId);
        Role role = roleRepositoryPort.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", roleName));
        
        user.addRole(role);
        user.setUpdatedAt(LocalDateTime.now());
        
        return userRepositoryPort.save(user);
    }
    
    @Override
    @Transactional
    @Auditable(action = Constants.ACTION_UPDATE, entityName = "User")
    public User removeRole(UUID userId, String roleName) {
        log.info("Removing role {} from user {}", roleName, userId);
        
        User user = getUserById(userId);
        Role role = roleRepositoryPort.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", roleName));
        
        user.removeRole(role);
        user.setUpdatedAt(LocalDateTime.now());
        
        return userRepositoryPort.save(user);
    }
    
    @Override
    public long countUsers() {
        return userRepositoryPort.count();
    }
}
