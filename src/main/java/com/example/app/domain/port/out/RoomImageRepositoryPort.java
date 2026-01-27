package com.example.app.domain.port.out;

import com.example.app.domain.model.RoomImage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port (repository interface) for room image persistence.
 * This is part of the domain layer and defines what the domain needs from image storage.
 * The actual implementation will be in the infrastructure layer.
 */
public interface RoomImageRepositoryPort {
    
    /**
     * Save a room image
     * @param roomImage Image to save
     * @return Saved image
     */
    RoomImage save(RoomImage roomImage);
    
    /**
     * Save multiple room images
     * @param roomImages List of images to save
     * @return List of saved images
     */
    List<RoomImage> saveAll(List<RoomImage> roomImages);
    
    /**
     * Find an image by ID
     * @param id Image ID
     * @return Optional containing the image if found
     */
    Optional<RoomImage> findById(UUID id);
    
    /**
     * Find all images for a room
     * @param roomId Room ID
     * @return List of images
     */
    List<RoomImage> findByRoomId(UUID roomId);
    
    /**
     * Find the main image for a room
     * @param roomId Room ID
     * @return Optional containing the main image if found
     */
    Optional<RoomImage> findMainImageByRoomId(UUID roomId);
    
    /**
     * Delete all images for a room
     * @param roomId Room ID
     */
    void deleteByRoomId(UUID roomId);
}
