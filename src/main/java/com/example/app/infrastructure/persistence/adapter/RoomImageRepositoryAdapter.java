package com.example.app.infrastructure.persistence.adapter;

import com.example.app.application.mapper.RoomImageMapper;
import com.example.app.domain.model.RoomImage;
import com.example.app.domain.port.out.RoomImageRepositoryPort;
import com.example.app.infrastructure.persistence.entity.RoomEntity;
import com.example.app.infrastructure.persistence.entity.RoomImageEntity;
import com.example.app.infrastructure.persistence.repository.RoomImageJpaRepository;
import com.example.app.infrastructure.persistence.repository.RoomJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter implementing RoomImageRepositoryPort.
 * Handles image persistence operations.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RoomImageRepositoryAdapter implements RoomImageRepositoryPort {
    
    private final RoomImageJpaRepository roomImageJpaRepository;
    private final RoomJpaRepository roomJpaRepository;
    private final RoomImageMapper roomImageMapper;
    
    @Override
    public RoomImage save(RoomImage roomImage) {
        log.debug("Saving room image: {}", roomImage.getFilename());
        RoomImageEntity entity = roomImageMapper.toEntity(roomImage);
        
        // Set room relationship
        RoomEntity room = roomJpaRepository.findById(roomImage.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomImage.getRoomId()));
        entity.setRoom(room);
        
        RoomImageEntity savedEntity = roomImageJpaRepository.save(entity);
        return roomImageMapper.toDomain(savedEntity);
    }
    
    @Override
    public List<RoomImage> saveAll(List<RoomImage> roomImages) {
        log.debug("Saving {} room images", roomImages.size());
        
        if (roomImages.isEmpty()) {
            return List.of();
        }
        
        UUID roomId = roomImages.get(0).getRoomId();
        RoomEntity room = roomJpaRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));
        
        List<RoomImageEntity> entities = roomImages.stream()
                .map(roomImageMapper::toEntity)
                .peek(entity -> entity.setRoom(room))
                .collect(Collectors.toList());
        
        List<RoomImageEntity> savedEntities = roomImageJpaRepository.saveAll(entities);
        return savedEntities.stream()
                .map(roomImageMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<RoomImage> findById(UUID id) {
        log.debug("Finding room image by ID: {}", id);
        return roomImageJpaRepository.findById(id)
                .map(roomImageMapper::toDomain);
    }
    
    @Override
    public List<RoomImage> findByRoomId(UUID roomId) {
        log.debug("Finding all images for room: {}", roomId);
        return roomImageJpaRepository.findByRoomId(roomId).stream()
                .map(roomImageMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<RoomImage> findMainImageByRoomId(UUID roomId) {
        log.debug("Finding main image for room: {}", roomId);
        return roomImageJpaRepository.findByRoomIdAndIsMainTrue(roomId)
                .map(roomImageMapper::toDomain);
    }
    
    @Override
    public void deleteByRoomId(UUID roomId) {
        log.debug("Deleting all images for room: {}", roomId);
        roomImageJpaRepository.deleteByRoomId(roomId);
    }
}
