package com.example.app.infrastructure.config;

import com.example.app.common.constants.Constants;
import com.example.app.domain.model.Role;
import com.example.app.domain.model.User;
import com.example.app.domain.port.out.RoleRepositoryPort;
import com.example.app.domain.port.out.UserRepositoryPort;
import com.example.app.domain.valueobject.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Initialize default roles and admin user on application startup
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final RoleRepositoryPort roleRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) {
        log.info("Initializing default data...");
        
        // Create default roles
        createRoleIfNotExists(Constants.ROLE_ADMIN, "Administrator role with full access");
        createRoleIfNotExists(Constants.ROLE_USER, "Standard user role");
        
        // Create default admin user
        createAdminUserIfNotExists();
        
        log.info("Data initialization completed");
    }
    
    private void createRoleIfNotExists(String roleName, String description) {
        if (!roleRepositoryPort.existsByName(roleName)) {
            Role role = Role.builder()
                    .name(roleName)
                    .description(description)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .deleted(false)
                    .build();
            
            roleRepositoryPort.save(role);
            log.info("Created role: {}", roleName);
        } else {
            log.debug("Role already exists: {}", roleName);
        }
    }
    
    private void createAdminUserIfNotExists() {
        String adminUsername = "admin";
        
        if (!userRepositoryPort.existsByUsername(adminUsername)) {
            Role adminRole = roleRepositoryPort.findByName(Constants.ROLE_ADMIN)
                    .orElseThrow(() -> new IllegalStateException("Admin role not found"));
            
            Role userRole = roleRepositoryPort.findByName(Constants.ROLE_USER)
                    .orElseThrow(() -> new IllegalStateException("User role not found"));
            
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            roles.add(userRole);
            
            User adminUser = User.builder()
                    .username(adminUsername)
                    .email(new Email("admin@example.com"))
                    .password(passwordEncoder.encode("admin123"))
                    .firstName("System")
                    .lastName("Administrator")
                    .enabled(true)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .roles(roles)
                    .provider(Constants.PROVIDER_LOCAL)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .deleted(false)
                    .build();
            
            userRepositoryPort.save(adminUser);
            log.info("Created default admin user - username: admin, password: admin123");
            log.warn("IMPORTANT: Please change the default admin password in production!");
        } else {
            log.debug("Admin user already exists");
        }
    }
}
