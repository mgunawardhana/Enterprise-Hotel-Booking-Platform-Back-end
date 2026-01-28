package com.example.app.domain.port.in;

import com.example.app.common.response.PageResponse;
import com.example.app.domain.model.Room;
import com.example.app.domain.valueobject.RoomSearchCriteria;

/**
 * Input port for searching rooms.
 * Defines the contract for room search use case.
 */
public interface SearchRoomsUseCase {
    
    /**
     * Search rooms based on provided criteria
     * 
     * @param criteria Search criteria including filters, sorting, and pagination
     * @return Paginated list of rooms matching the criteria
     */
    PageResponse<Room> searchRooms(RoomSearchCriteria criteria);
}
