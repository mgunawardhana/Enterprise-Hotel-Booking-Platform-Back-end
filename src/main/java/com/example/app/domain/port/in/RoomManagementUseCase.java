package com.example.app.domain.port.in;

import com.example.app.domain.model.Room;
import com.example.app.domain.model.RoomImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Input port (use case) for room management operations.
 * Defines the contract for room business logic.
 * This is part of the domain layer and represents what the application can do.
 */
public interface RoomManagementUseCase {
    
    /**
     * Create a new room
     * @param room Room domain object to create
     * @return Created room with generated ID
     */
    Room createRoom(Room room);
    
    /**
     * Update an existing room
     * @param id Room ID
     * @param room Updated room data
     * @return Updated room
     */
    Room updateRoom(UUID id, Room room);
    
    /**
     * Soft delete a room
     * @param id Room ID to delete
     */
    void deleteRoom(UUID id);
    
    /**
     * Get a single room by ID
     * @param id Room ID
     * @return Room domain object
     */
    Room getRoomById(UUID id);
    
    /**
     * Get all rooms with pagination
     * @param pageable Pagination parameters
     * @return Page of rooms
     */
    Page<Room> getAllRooms(Pageable pageable);
    
    /**
     * Upload images for a room
     * @param roomId Room ID
     * @param images List of room images
     * @return List of saved images
     */
    List<RoomImage> uploadRoomImages(UUID roomId, List<RoomImage> images);
    
    /**
     * Get all images for a room
     * @param roomId Room ID
     * @return List of room images
     */
    List<RoomImage> getRoomImages(UUID roomId);
    
    /**
     * Get a single image by ID
     * @param imageId Image ID
     * @return Room image with binary data
     */
    RoomImage getRoomImageById(UUID imageId);
}
