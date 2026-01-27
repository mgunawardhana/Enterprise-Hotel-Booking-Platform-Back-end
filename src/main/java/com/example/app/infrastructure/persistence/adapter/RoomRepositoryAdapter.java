package com.example.app.infrastructure.persistence.adapter;

import com.example.app.application.mapper.RoomMapper;
import com.example.app.domain.model.Room;
import com.example.app.domain.port.out.RoomRepositoryPort;
import com.example.app.infrastructure.persistence.entity.RoomEntity;
import com.example.app.infrastructure.persistence.repository.RoomJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Adapter implementing RoomRepositoryPort.
 * Bridges the domain layer with the infrastructure (JPA) layer.
 * This is where the hexagonal architecture's "output adapter" lives.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RoomRepositoryAdapter implements RoomRepositoryPort {
    
    private final RoomJpaRepository roomJpaRepository;
    private final RoomMapper roomMapper;
    
    @Override
    public Room save(Room room) {
        log.debug("Saving room: {}", room.getTitle());
        RoomEntity entity = roomMapper.toEntity(room);
        RoomEntity savedEntity = roomJpaRepository.save(entity);
        return roomMapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<Room> findById(UUID id) {
        log.debug("Finding room by ID: {}", id);
        return roomJpaRepository.findByIdAndDeletedFalse(id)
                .map(roomMapper::toDomain);
    }
    
    @Override
    public Page<Room> findAll(Pageable pageable) {
        log.debug("Finding all rooms with pagination: page={}, size={}", 
                  pageable.getPageNumber(), pageable.getPageSize());
        Page<RoomEntity> entities = roomJpaRepository.findAllByDeletedFalse(pageable);
        return entities.map(roomMapper::toDomain);
    }
    
    @Override
    public boolean existsById(UUID id) {
        log.debug("Checking if room exists: {}", id);
        return roomJpaRepository.existsByIdAndDeletedFalse(id);
    }
    
    @Override
    public void deleteById(UUID id) {
        log.debug("Soft deleting room: {}", id);
        roomJpaRepository.findByIdAndDeletedFalse(id).ifPresent(entity -> {
            entity.setDeleted(true);
            roomJpaRepository.save(entity);
        });
    }
}
