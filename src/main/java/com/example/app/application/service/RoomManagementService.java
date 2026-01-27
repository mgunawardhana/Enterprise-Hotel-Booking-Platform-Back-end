package com.example.app.application.service;

import com.example.app.domain.model.Room;
import com.example.app.domain.model.RoomImage;
import com.example.app.domain.port.in.RoomManagementUseCase;
import com.example.app.domain.port.out.RoomImageRepositoryPort;
import com.example.app.domain.port.out.RoomRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service implementing RoomManagementUseCase.
 * Contains business logic for room management operations.
 * This is the application service layer in hexagonal architecture.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoomManagementService implements RoomManagementUseCase {
    
    private final RoomRepositoryPort roomRepositoryPort;
    private final RoomImageRepositoryPort roomImageRepositoryPort;
    
    @Override
    @Transactional
    public Room createRoom(Room room) {
        log.info("Creating new room: {}", room.getTitle());
        
        // Business validation
        validateRoom(room);
        
        Room savedRoom = roomRepositoryPort.save(room);
        log.info("Room created successfully with ID: {}", savedRoom.getId());
        
        return savedRoom;
    }
    
    @Override
    @Transactional
    public Room updateRoom(UUID id, Room room) {
        log.info("Updating room: {}", id);
        
        // Check if room exists
        Room existingRoom = roomRepositoryPort.findById(id)
                .orElseThrow(() -> {
                    log.error("Room not found: {}", id);
                    return new RuntimeException("Room not found with ID: " + id);
                });
        
        // Business validation
        validateRoom(room);
        
        // Update fields
        room.setId(id);
        room.setCreatedAt(existingRoom.getCreatedAt());
        room.setCreatedBy(existingRoom.getCreatedBy());
        
        Room updatedRoom = roomRepositoryPort.save(room);
        log.info("Room updated successfully: {}", id);
        
        return updatedRoom;
    }
    
    @Override
    @Transactional
    public void deleteRoom(UUID id) {
        log.info("Deleting room: {}", id);
        
        // Check if room exists
        if (!roomRepositoryPort.existsById(id)) {
            log.error("Room not found: {}", id);
            throw new RuntimeException("Room not found with ID: " + id);
        }
        
        roomRepositoryPort.deleteById(id);
        log.info("Room soft deleted successfully: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Room getRoomById(UUID id) {
        log.info("Fetching room: {}", id);
        
        return roomRepositoryPort.findById(id)
                .orElseThrow(() -> {
                    log.error("Room not found: {}", id);
                    return new RuntimeException("Room not found with ID: " + id);
                });
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Room> getAllRooms(Pageable pageable) {
        log.info("Fetching all rooms - page: {}, size: {}, sort: {}", 
                 pageable.getPageNumber(), 
                 pageable.getPageSize(), 
                 pageable.getSort());
        
        Page<Room> rooms = roomRepositoryPort.findAll(pageable);
        log.info("Fetched {} rooms out of {} total", rooms.getNumberOfElements(), rooms.getTotalElements());
        
        return rooms;
    }
    
    @Override
    @Transactional
    public List<RoomImage> uploadRoomImages(UUID roomId, List<RoomImage> images) {
        log.info("Uploading {} images for room: {}", images.size(), roomId);
        
        // Verify room exists
        if (!roomRepositoryPort.existsById(roomId)) {
            log.error("Room not found: {}", roomId);
            throw new RuntimeException("Room not found with ID: " + roomId);
        }
        
        // Set room ID for all images
        images.forEach(image -> image.setRoomId(roomId));
        
        // If one of the images is marked as main, unset any existing main image
        boolean hasMainImage = images.stream().anyMatch(RoomImage::isMain);
        if (hasMainImage) {
            roomImageRepositoryPort.findMainImageByRoomId(roomId).ifPresent(existingMain -> {
                existingMain.setMain(false);
                roomImageRepositoryPort.save(existingMain);
            });
        }
        
        List<RoomImage> savedImages = roomImageRepositoryPort.saveAll(images);
        log.info("Successfully uploaded {} images for room: {}", savedImages.size(), roomId);
        
        return savedImages;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RoomImage> getRoomImages(UUID roomId) {
        log.info("Fetching images for room: {}", roomId);
        
        // Verify room exists
        if (!roomRepositoryPort.existsById(roomId)) {
            log.error("Room not found: {}", roomId);
            throw new RuntimeException("Room not found with ID: " + roomId);
        }
        
        List<RoomImage> images = roomImageRepositoryPort.findByRoomId(roomId);
        log.info("Found {} images for room: {}", images.size(), roomId);
        
        return images;
    }
    
    @Override
    @Transactional(readOnly = true)
    public RoomImage getRoomImageById(UUID imageId) {
        log.info("Fetching room image: {}", imageId);
        
        return roomImageRepositoryPort.findById(imageId)
                .orElseThrow(() -> {
                    log.error("Room image not found: {}", imageId);
                    return new RuntimeException("Room image not found with ID: " + imageId);
                });
    }
    
    /**
     * Business validation for room
     */
    private void validateRoom(Room room) {
        if (room.getPricePerNight() != null && room.getPricePerNight().doubleValue() <= 0) {
            throw new IllegalArgumentException("Price per night must be greater than 0");
        }
        
        if (room.getRating() != null && (room.getRating() < 0 || room.getRating() > 5)) {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }
        
        if (room.getMaxGuests() != null && room.getMaxGuests() <= 0) {
            throw new IllegalArgumentException("Max guests must be greater than 0");
        }
    }
}
