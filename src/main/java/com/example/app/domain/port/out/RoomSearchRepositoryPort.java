package com.example.app.domain.port.out;

import com.example.app.domain.model.Room;
import com.example.app.domain.valueobject.RoomSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Output port for room search repository operations.
 * Defines the contract for persistence layer.
 */
public interface RoomSearchRepositoryPort {
    
    /**
     * Find rooms matching the search criteria
     * 
     * @param criteria Search criteria with filters
     * @param pageable Pagination and sorting information
     * @return Page of rooms matching the criteria
     */
    Page<Room> findByCriteria(RoomSearchCriteria criteria, Pageable pageable);
}
