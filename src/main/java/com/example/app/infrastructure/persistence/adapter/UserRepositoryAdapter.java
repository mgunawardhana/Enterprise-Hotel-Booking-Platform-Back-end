package com.example.app.infrastructure.persistence.adapter;

import com.example.app.domain.model.Role;
import com.example.app.domain.model.User;
import com.example.app.domain.port.out.UserRepositoryPort;
import com.example.app.domain.valueobject.Email;
import com.example.app.infrastructure.persistence.entity.RoleEntity;
import com.example.app.infrastructure.persistence.entity.UserEntity;
import com.example.app.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter that implements UserRepositoryPort using JPA.
 * Bridges domain layer with infrastructure persistence.
 */
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {
    
    private final UserJpaRepository userJpaRepository;
    
    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        UserEntity saved = userJpaRepository.save(entity);
        return toDomain(saved);
    }
    
    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id)
                .map(this::toDomain);
    }
    
    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username)
                .map(this::toDomain);
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(this::toDomain);
    }
    
    @Override
    public Optional<User> findByProviderAndProviderId(String provider, String providerId) {
        return userJpaRepository.findByProviderAndProviderId(provider, providerId)
                .map(this::toDomain);
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }
    
    @Override
    public List<User> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userJpaRepository.findAllActive(pageable)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public long count() {
        return userJpaRepository.countActive();
    }
    
    @Override
    public void delete(UUID id) {
        userJpaRepository.findById(id).ifPresent(user -> {
            user.setDeleted(true);
            userJpaRepository.save(user);
        });
    }
    
    /**
     * Convert domain User to UserEntity
     */
    private UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail() != null ? user.getEmail().getValue() : null)
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .profilePicture(user.getProfilePicture())
                .enabled(user.isEnabled())
                .accountNonExpired(user.isAccountNonExpired())
                .accountNonLocked(user.isAccountNonLocked())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .roles(user.getRoles().stream()
                        .map(this::toRoleEntity)
                        .collect(Collectors.toSet()))
                .provider(user.getProvider())
                .providerId(user.getProviderId())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .createdBy(user.getCreatedBy())
                .updatedBy(user.getUpdatedBy())
                .deleted(user.isDeleted())
                .build();
    }
    
    /**
     * Convert UserEntity to domain User
     */
    private User toDomain(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail() != null ? new Email(entity.getEmail()) : null)
                .password(entity.getPassword())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .profilePicture(entity.getProfilePicture())
                .enabled(entity.isEnabled())
                .accountNonExpired(entity.isAccountNonExpired())
                .accountNonLocked(entity.isAccountNonLocked())
                .credentialsNonExpired(entity.isCredentialsNonExpired())
                .roles(entity.getRoles().stream()
                        .map(this::toRoleDomain)
                        .collect(Collectors.toSet()))
                .provider(entity.getProvider())
                .providerId(entity.getProviderId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .deleted(entity.isDeleted())
                .build();
    }
    
    private RoleEntity toRoleEntity(Role role) {
        return RoleEntity.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .build();
    }
    
    private Role toRoleDomain(RoleEntity entity) {
        return Role.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deleted(entity.isDeleted())
                .build();
    }
}
