package com.example.app.application.service;

import com.example.app.common.response.PageResponse;
import com.example.app.domain.model.Room;
import com.example.app.domain.port.in.SearchRoomsUseCase;
import com.example.app.domain.port.out.RoomSearchRepositoryPort;
import com.example.app.domain.valueobject.RoomSearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service implementing room search use case.
 * Coordinates search operations and applies business logic.
 */
@Service
@Transactional(readOnly = true)
public class RoomSearchService implements SearchRoomsUseCase {
    
    private static final Logger log = LoggerFactory.getLogger(RoomSearchService.class);
    
    private final RoomSearchRepositoryPort roomSearchRepository;
    
    public RoomSearchService(RoomSearchRepositoryPort roomSearchRepository) {
        this.roomSearchRepository = roomSearchRepository;
    }
    
    @Override
    public PageResponse<Room> searchRooms(RoomSearchCriteria criteria) {
        long startTime = System.currentTimeMillis();
        
        log.info("Searching rooms with criteria - minPrice: {}, maxPrice: {}, minGuests: {}, bedTypes: {}, " +
                "amenities: {}, views: {}, checkIn: {}, checkOut: {}, sortBy: {}, sortDirection: {}, page: {}, size: {}",
                criteria.getMinPrice(), criteria.getMaxPrice(), criteria.getMinGuests(), criteria.getBedTypes(),
                criteria.getAmenities(), criteria.getViews(), criteria.getCheckIn(), criteria.getCheckOut(),
                criteria.getSortBy(), criteria.getSortDirection(), criteria.getPage(), criteria.getSize());
        
        // Create pageable with sorting
        Pageable pageable = createPageable(criteria);
        
        // Execute search
        Page<Room> roomPage = roomSearchRepository.findByCriteria(criteria, pageable);
        
        long executionTime = System.currentTimeMillis() - startTime;
        log.info("Room search completed in {}ms. Found {} rooms (page {} of {})",
                executionTime, roomPage.getTotalElements(), roomPage.getNumber(), roomPage.getTotalPages());
        
        // Convert to PageResponse
        return PageResponse.of(
                roomPage.getContent(),
                roomPage.getNumber(),
                roomPage.getSize(),
                roomPage.getTotalElements()
        );
    }
    
    /**
     * Creates Pageable object with sorting based on criteria
     */
    private Pageable createPageable(RoomSearchCriteria criteria) {
        Sort sort = createSort(criteria.getSortBy(), criteria.getSortDirection());
        return PageRequest.of(criteria.getPage(), criteria.getSize(), sort);
    }
    
    /**
     * Creates Sort object based on field and direction
     */
    private Sort createSort(String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") 
                ? Sort.Direction.DESC 
                : Sort.Direction.ASC;
        
        // Map sort fields to entity fields
        String entityField = switch (sortBy.toLowerCase()) {
            case "price" -> "pricePerNight";
            case "rating" -> "rating";
            case "popularity" -> "rating"; // Using rating as proxy for popularity
            default -> "pricePerNight";
        };
        
        return Sort.by(direction, entityField);
    }
}
