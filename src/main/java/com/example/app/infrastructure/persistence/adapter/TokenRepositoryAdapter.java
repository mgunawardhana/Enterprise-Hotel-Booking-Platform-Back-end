package com.example.app.infrastructure.persistence.adapter;

import com.example.app.domain.port.out.TokenRepositoryPort;
import com.example.app.infrastructure.persistence.entity.RefreshTokenEntity;
import com.example.app.infrastructure.persistence.repository.RefreshTokenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Adapter that implements TokenRepositoryPort using JPA
 */
@Component
@RequiredArgsConstructor
public class TokenRepositoryAdapter implements TokenRepositoryPort {
    
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;
    
    @Override
    @Transactional
    public void saveRefreshToken(UUID userId, String token, long expirationMs) {
        RefreshTokenEntity entity = RefreshTokenEntity.builder()
                .token(token)
                .userId(userId)
                .expiryDate(LocalDateTime.now().plusSeconds(expirationMs / 1000))
                .build();
        
        refreshTokenJpaRepository.save(entity);
    }
    
    @Override
    public Optional<UUID> findUserIdByRefreshToken(String token) {
        return refreshTokenJpaRepository.findByToken(token)
                .filter(t -> !t.isExpired())
                .map(RefreshTokenEntity::getUserId);
    }
    
    @Override
    @Transactional
    public void deleteRefreshToken(String token) {
        refreshTokenJpaRepository.findByToken(token)
                .ifPresent(refreshTokenJpaRepository::delete);
    }
    
    @Override
    @Transactional
    public void deleteAllRefreshTokensForUser(UUID userId) {
        refreshTokenJpaRepository.deleteByUserId(userId);
    }
    
    @Override
    public boolean isRefreshTokenValid(String token) {
        return refreshTokenJpaRepository.findByToken(token)
                .map(t -> !t.isExpired())
                .orElse(false);
    }
}
