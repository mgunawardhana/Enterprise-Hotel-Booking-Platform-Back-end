package com.example.app.domain.port.in;

import com.example.app.domain.model.User;

/**
 * Input port (use case interface) for authentication operations.
 * Defines what the application can do regarding authentication.
 */
public interface AuthenticationUseCase {
    
    /**
     * Register a new user
     */
    User register(String username, String email, String password, String firstName, String lastName);
    
    /**
     * Authenticate user with username/email and password
     */
    User authenticate(String usernameOrEmail, String password);
    
    /**
     * Register or login user via OAuth2 (Google)
     */
    User authenticateOAuth(String email, String name, String provider, String providerId, String profilePicture);
    
    /**
     * Validate refresh token and return user
     */
    User validateRefreshToken(String refreshToken);
    
    /**
     * Invalidate refresh token (logout)
     */
    void invalidateRefreshToken(String refreshToken);
}
