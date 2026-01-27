package com.example.app.domain.port.out;

import com.example.app.domain.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Output port (repository interface) for room persistence.
 * This is part of the domain layer and defines what the domain needs from persistence.
 * The actual implementation will be in the infrastructure layer.
 */
public interface RoomRepositoryPort {
    
    /**
     * Save a room (create or update)
     * @param room Room to save
     * @return Saved room
     */
    Room save(Room room);
    
    /**
     * Find a room by ID (excluding soft-deleted)
     * @param id Room ID
     * @return Optional containing the room if found
     */
    Optional<Room> findById(UUID id);
    
    /**
     * Find all rooms with pagination (excluding soft-deleted)
     * @param pageable Pagination parameters
     * @return Page of rooms
     */
    Page<Room> findAll(Pageable pageable);
    
    /**
     * Check if a room exists by ID (excluding soft-deleted)
     * @param id Room ID
     * @return true if exists, false otherwise
     */
    boolean existsById(UUID id);
    
    /**
     * Soft delete a room
     * @param id Room ID
     */
    void deleteById(UUID id);
}
