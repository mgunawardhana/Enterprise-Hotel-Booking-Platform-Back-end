package com.example.app.infrastructure.persistence.adapter;

import com.example.app.domain.model.Role;
import com.example.app.domain.port.out.RoleRepositoryPort;
import com.example.app.infrastructure.persistence.entity.RoleEntity;
import com.example.app.infrastructure.persistence.repository.RoleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Adapter that implements RoleRepositoryPort using JPA
 */
@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepositoryPort {
    
    private final RoleJpaRepository roleJpaRepository;
    
    @Override
    public Role save(Role role) {
        RoleEntity entity = toEntity(role);
        RoleEntity saved = roleJpaRepository.save(entity);
        return toDomain(saved);
    }
    
    @Override
    public Optional<Role> findById(UUID id) {
        return roleJpaRepository.findById(id)
                .map(this::toDomain);
    }
    
    @Override
    public Optional<Role> findByName(String name) {
        return roleJpaRepository.findByName(name)
                .map(this::toDomain);
    }
    
    @Override
    public boolean existsByName(String name) {
        return roleJpaRepository.existsByName(name);
    }
    
    private RoleEntity toEntity(Role role) {
        return RoleEntity.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .deleted(role.isDeleted())
                .build();
    }
    
    private Role toDomain(RoleEntity entity) {
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
