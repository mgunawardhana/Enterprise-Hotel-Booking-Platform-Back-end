package com.example.app.domain.port.out;

import java.util.Optional;
import java.util.UUID;

/**
 * Output port for refresh token operations.
 */
public interface TokenRepositoryPort {
    
    /**
     * Save refresh token for user
     */
    void saveRefreshToken(UUID userId, String token, long expirationMs);
    
    /**
     * Find user ID by refresh token
     */
    Optional<UUID> findUserIdByRefreshToken(String token);
    
    /**
     * Delete refresh token
     */
    void deleteRefreshToken(String token);
    
    /**
     * Delete all refresh tokens for user
     */
    void deleteAllRefreshTokensForUser(UUID userId);
    
    /**
     * Check if refresh token exists and is valid
     */
    boolean isRefreshTokenValid(String token);
}
