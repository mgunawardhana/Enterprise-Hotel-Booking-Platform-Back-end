package com.example.app.adapter.web.controller;

import com.example.app.adapter.web.request.RoomSearchRequest;
import com.example.app.adapter.web.response.RoomSearchResponse;
import com.example.app.application.mapper.RoomSearchMapper;
import com.example.app.common.response.CommonResponse;
import com.example.app.common.response.PageResponse;
import com.example.app.common.util.TraceIdGenerator;
import com.example.app.domain.model.Room;
import com.example.app.domain.port.in.SearchRoomsUseCase;
import com.example.app.domain.port.out.RoomImageRepositoryPort;
import com.example.app.domain.valueobject.RoomSearchCriteria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * PUBLIC REST controller for room search operations.
 * No authentication required - accessible to all users.
 */
@RestController
@RequestMapping("/api/v1/rooms")
@Tag(name = "Room Search", description = "Public room search and filtering API")
public class SearchRoomController {
    
    private static final Logger log = LoggerFactory.getLogger(SearchRoomController.class);
    
    private final SearchRoomsUseCase searchRoomsUseCase;
    private final RoomSearchMapper roomSearchMapper;
    private final RoomImageRepositoryPort roomImageRepository;
    
    public SearchRoomController(SearchRoomsUseCase searchRoomsUseCase,
                                RoomSearchMapper roomSearchMapper,
                                RoomImageRepositoryPort roomImageRepository) {
        this.searchRoomsUseCase = searchRoomsUseCase;
        this.roomSearchMapper = roomSearchMapper;
        this.roomImageRepository = roomImageRepository;
    }
    
    /**
     * Search rooms with advanced filtering, sorting, and pagination
     * PUBLIC endpoint - no authentication required
     */
    @GetMapping("/search")
    @Operation(summary = "Search rooms", description = "Search and filter rooms with pagination")
    public ResponseEntity<CommonResponse<PageResponse<RoomSearchResponse>>> searchRooms(
            @Valid @ModelAttribute RoomSearchRequest request) {
        
        String traceId = TraceIdGenerator.generate();
        long startTime = System.currentTimeMillis();
        
        log.info("[{}] Room search request received: {}", traceId, request);
        
        try {
            // Convert request to search criteria
            RoomSearchCriteria criteria = roomSearchMapper.toSearchCriteria(request);
            
            // Execute search
            PageResponse<Room> roomPage = searchRoomsUseCase.searchRooms(criteria);
            
            // Convert to response DTOs with main images
            List<RoomSearchResponse> responseList = roomPage.getContent().stream()
                    .map(room -> {
                        RoomSearchResponse response = roomSearchMapper.toSearchResponse(room);
                        
                        // Fetch and set main image URL (lazy loading for performance)
                        roomImageRepository.findMainImageByRoomId(room.getId())
                                .ifPresent(image -> {
                                    // Construct image URL: /api/v1/rooms/{roomId}/images/{imageId}
                                    String imageUrl = "/api/v1/rooms/" + room.getId() + "/images/" + image.getId();
                                    response.setMainImage(imageUrl);
                                });
                        
                        return response;
                    })
                    .collect(Collectors.toList());
            
            // Create paginated response
            PageResponse<RoomSearchResponse> pageResponse = PageResponse.of(
                    responseList,
                    roomPage.getPage(),
                    roomPage.getSize(),
                    roomPage.getTotalElements()
            );
            
            long executionTime = System.currentTimeMillis() - startTime;
            log.info("[{}] Room search completed in {}ms. Found {} rooms", 
                    traceId, executionTime, roomPage.getTotalElements());
            
            return ResponseEntity.ok(
                    CommonResponse.success(
                            pageResponse,
                            "Rooms fetched successfully",
                            HttpStatus.OK.value(),
                            traceId
                    )
            );
            
        } catch (IllegalArgumentException e) {
            log.error("[{}] Invalid search criteria: {}", traceId, e.getMessage());
            return ResponseEntity.badRequest().body(
                    CommonResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value(), traceId)
            );
        } catch (Exception e) {
            log.error("[{}] Error searching rooms", traceId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    CommonResponse.error(
                            "An error occurred while searching rooms",
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            traceId
                    )
            );
        }
    }
}
