package com.example.app.application.service;

import com.example.app.common.constants.Constants;
import com.example.app.common.exception.DuplicateResourceException;
import com.example.app.common.exception.InvalidTokenException;
import com.example.app.domain.model.Role;
import com.example.app.domain.model.User;
import com.example.app.domain.port.in.AuthenticationUseCase;
import com.example.app.domain.port.out.RoleRepositoryPort;
import com.example.app.domain.port.out.TokenRepositoryPort;
import com.example.app.domain.port.out.UserRepositoryPort;
import com.example.app.domain.valueobject.Email;
import com.example.app.infrastructure.audit.Auditable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Application service implementing authentication use cases
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationUseCase {
    
    private final UserRepositoryPort userRepositoryPort;
    private final RoleRepositoryPort roleRepositoryPort;
    private final TokenRepositoryPort tokenRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional
    @Auditable(action = Constants.ACTION_REGISTER, entityName = "User")
    public User register(String username, String email, String password, String firstName, String lastName) {
        log.info("Registering new user: {}", username);
        
        // Validate uniqueness
        if (userRepositoryPort.existsByUsername(username)) {
            throw new DuplicateResourceException("User", "username", username);
        }
        
        if (userRepositoryPort.existsByEmail(email)) {
            throw new DuplicateResourceException("User", "email", email);
        }
        
        // Get default USER role
        Role userRole = roleRepositoryPort.findByName(Constants.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("Default USER role not found"));
        
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        
        // Create user
        User user = User.builder()
                .username(username)
                .email(new Email(email))
                .password(passwordEncoder.encode(password))
                .firstName(firstName)
                .lastName(lastName)
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
        
        User savedUser = userRepositoryPort.save(user);
        log.info("User registered successfully: {}", username);
        
        return savedUser;
    }
    
    @Override
    @Auditable(action = Constants.ACTION_LOGIN, entityName = "User")
    public User authenticate(String usernameOrEmail, String password) {
        log.info("Authenticating user: {}", usernameOrEmail);
        
        // Find user by username or email
        User user = userRepositoryPort.findByUsername(usernameOrEmail)
                .or(() -> userRepositoryPort.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new BadCredentialsException("Invalid username/email or password"));
        
        // Validate password
        if (user.getPassword() == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid username/email or password");
        }
        
        // Validate account status
        if (!user.isAccountValid()) {
            throw new BadCredentialsException("Account is disabled or locked");
        }
        
        log.info("User authenticated successfully: {}", user.getUsername());
        return user;
    }
    
    @Override
    @Transactional
    @Auditable(action = Constants.ACTION_LOGIN, entityName = "User")
    public User authenticateOAuth(String email, String name, String provider, String providerId, String profilePicture) {
        log.info("OAuth authentication for email: {}", email);
        
        // Check if user exists with this provider
        User user = userRepositoryPort.findByProviderAndProviderId(provider, providerId)
                .orElseGet(() -> {
                    // Check if user exists with this email
                    return userRepositoryPort.findByEmail(email)
                            .map(existingUser -> {
                                // Update existing user with OAuth info
                                existingUser.setProvider(provider);
                                existingUser.setProviderId(providerId);
                                existingUser.setProfilePicture(profilePicture);
                                return userRepositoryPort.save(existingUser);
                            })
                            .orElseGet(() -> {
                                // Create new user
                                Role userRole = roleRepositoryPort.findByName(Constants.ROLE_USER)
                                        .orElseThrow(() -> new IllegalStateException("Default USER role not found"));
                                
                                Set<Role> roles = new HashSet<>();
                                roles.add(userRole);
                                
                                String username = email.split("@")[0] + "_" + UUID.randomUUID().toString().substring(0, 8);
                                
                                User newUser = User.builder()
                                        .username(username)
                                        .email(new Email(email))
                                        .firstName(name)
                                        .profilePicture(profilePicture)
                                        .enabled(true)
                                        .accountNonExpired(true)
                                        .accountNonLocked(true)
                                        .credentialsNonExpired(true)
                                        .roles(roles)
                                        .provider(provider)
                                        .providerId(providerId)
                                        .createdAt(LocalDateTime.now())
                                        .updatedAt(LocalDateTime.now())
                                        .deleted(false)
                                        .build();
                                
                                return userRepositoryPort.save(newUser);
                            });
                });
        
        log.info("OAuth authentication successful for: {}", user.getUsername());
        return user;
    }
    
    @Override
    @Auditable(action = Constants.ACTION_TOKEN_REFRESH, entityName = "User")
    public User validateRefreshToken(String refreshToken) {
        log.debug("Validating refresh token");
        
        UUID userId = tokenRepositoryPort.findUserIdByRefreshToken(refreshToken)
                .orElseThrow(() -> new InvalidTokenException("Invalid or expired refresh token"));
        
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new InvalidTokenException("User not found for refresh token"));
        
        if (!user.isAccountValid()) {
            throw new InvalidTokenException("Account is disabled or locked");
        }
        
        return user;
    }
    
    @Override
    @Transactional
    @Auditable(action = Constants.ACTION_LOGOUT, entityName = "User")
    public void invalidateRefreshToken(String refreshToken) {
        log.debug("Invalidating refresh token");
        tokenRepositoryPort.deleteRefreshToken(refreshToken);
    }
}
