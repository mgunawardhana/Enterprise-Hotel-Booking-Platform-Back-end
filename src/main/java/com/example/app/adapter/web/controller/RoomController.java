package com.example.app.adapter.web.controller;

import com.example.app.adapter.web.request.CreateRoomRequest;
import com.example.app.adapter.web.request.UpdateRoomRequest;
import com.example.app.adapter.web.response.RoomDetailResponse;
import com.example.app.adapter.web.response.RoomResponse;
import com.example.app.common.response.CommonResponse;
import com.example.app.common.response.PageResponse;
import com.example.app.common.util.TraceIdGenerator;
import com.example.app.domain.model.Room;
import com.example.app.domain.port.in.RoomManagementUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST controller for room management operations.
 * All endpoints return CommonResponse wrapper.
 * Public endpoints: GET (for website/landing pages)
 * Admin endpoints: POST, PUT, DELETE (future: add @PreAuthorize)
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
@Tag(name = "Room Management", description = "APIs for managing hotel rooms")
public class RoomController {
    
    private final RoomManagementUseCase roomManagementUseCase;
    
    /**
     * PUBLIC: Get all rooms with pagination and sorting
     */
    @GetMapping
    @Operation(summary = "Get all rooms", description = "Retrieve paginated list of rooms with optional sorting")
    public ResponseEntity<CommonResponse<PageResponse<RoomResponse>>> getAllRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        String traceId = TraceIdGenerator.generate();
        log.info("[{}] GET /api/v1/rooms - page: {}, size: {}, sortBy: {}, sortDirection: {}", 
                 traceId, page, size, sortBy, sortDirection);
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<Room> roomPage = roomManagementUseCase.getAllRooms(pageable);
        
        List<RoomResponse> roomResponses = roomPage.getContent().stream()
                .map(this::toRoomResponse)
                .collect(Collectors.toList());
        
        PageResponse<RoomResponse> pageResponse = PageResponse.of(
                roomResponses,
                roomPage.getNumber(),
                roomPage.getSize(),
                roomPage.getTotalElements()
        );
        
        log.info("[{}] Successfully fetched {} rooms", traceId, roomResponses.size());
        
        return ResponseEntity.ok(CommonResponse.success(
                pageResponse,
                "Rooms fetched successfully",
                HttpStatus.OK.value(),
                traceId
        ));
    }
    
    /**
     * PUBLIC: Get single room by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get room by ID", description = "Retrieve detailed information about a specific room")
    public ResponseEntity<CommonResponse<RoomDetailResponse>> getRoomById(@PathVariable UUID id) {
        String traceId = TraceIdGenerator.generate();
        log.info("[{}] GET /api/v1/rooms/{}", traceId, id);
        
        Room room = roomManagementUseCase.getRoomById(id);
        RoomDetailResponse response = toRoomDetailResponse(room);
        
        log.info("[{}] Successfully fetched room: {}", traceId, room.getTitle());
        
        return ResponseEntity.ok(CommonResponse.success(
                response,
                "Room fetched successfully",
                HttpStatus.OK.value(),
                traceId
        ));
    }
    
    /**
     * ADMIN: Create new room
     * Future: Add @PreAuthorize("hasRole('ADMIN')")
     */
    @PostMapping
    @Operation(summary = "Create room", description = "Create a new room (Admin only)")
    public ResponseEntity<CommonResponse<RoomResponse>> createRoom(
            @Valid @RequestBody CreateRoomRequest request
    ) {
        String traceId = TraceIdGenerator.generate();
        log.info("[{}] POST /api/v1/rooms - title: {}", traceId, request.getTitle());
        
        Room room = toRoomDomain(request);
        Room createdRoom = roomManagementUseCase.createRoom(room);
        RoomResponse response = toRoomResponse(createdRoom);
        
        log.info("[{}] Successfully created room with ID: {}", traceId, createdRoom.getId());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(
                response,
                "Room created successfully",
                HttpStatus.CREATED.value(),
                traceId
        ));
    }
    
    /**
     * ADMIN: Update existing room
     * Future: Add @PreAuthorize("hasRole('ADMIN')")
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update room", description = "Update an existing room (Admin only)")
    public ResponseEntity<CommonResponse<RoomResponse>> updateRoom(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateRoomRequest request
    ) {
        String traceId = TraceIdGenerator.generate();
        log.info("[{}] PUT /api/v1/rooms/{}", traceId, id);
        
        Room room = toRoomDomain(request);
        Room updatedRoom = roomManagementUseCase.updateRoom(id, room);
        RoomResponse response = toRoomResponse(updatedRoom);
        
        log.info("[{}] Successfully updated room: {}", traceId, id);
        
        return ResponseEntity.ok(CommonResponse.success(
                response,
                "Room updated successfully",
                HttpStatus.OK.value(),
                traceId
        ));
    }
    
    /**
     * ADMIN: Delete room (soft delete)
     * Future: Add @PreAuthorize("hasRole('ADMIN')")
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete room", description = "Soft delete a room (Admin only)")
    public ResponseEntity<CommonResponse<Void>> deleteRoom(@PathVariable UUID id) {
        String traceId = TraceIdGenerator.generate();
        log.info("[{}] DELETE /api/v1/rooms/{}", traceId, id);
        
        roomManagementUseCase.deleteRoom(id);
        
        log.info("[{}] Successfully deleted room: {}", traceId, id);
        
        return ResponseEntity.ok(CommonResponse.success(
                null,
                "Room deleted successfully",
                HttpStatus.OK.value(),
                traceId
        ));
    }
    
    // Mapping methods
    
    private Room toRoomDomain(CreateRoomRequest request) {
        Room room = new Room();
        room.setTitle(request.getTitle());
        room.setDescription(request.getDescription());
        room.setPricePerNight(request.getPricePerNight());
        room.setRating(request.getRating());
        room.setMaxGuests(request.getMaxGuests());
        room.setBedType(request.getBedType());
        room.setRoomSize(request.getRoomSize());
        room.setTags(request.getTags());
        room.setAmenities(request.getAmenities());
        room.setBadges(request.getBadges());
        room.setStatus(request.getStatus());
        return room;
    }
    
    private Room toRoomDomain(UpdateRoomRequest request) {
        Room room = new Room();
        room.setTitle(request.getTitle());
        room.setDescription(request.getDescription());
        room.setPricePerNight(request.getPricePerNight());
        room.setRating(request.getRating());
        room.setMaxGuests(request.getMaxGuests());
        room.setBedType(request.getBedType());
        room.setRoomSize(request.getRoomSize());
        room.setTags(request.getTags());
        room.setAmenities(request.getAmenities());
        room.setBadges(request.getBadges());
        room.setStatus(request.getStatus());
        return room;
    }
    
    private RoomResponse toRoomResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .title(room.getTitle())
                .description(room.getDescription())
                .pricePerNight(room.getPricePerNight())
                .rating(room.getRating())
                .maxGuests(room.getMaxGuests())
                .bedType(room.getBedType())
                .roomSize(room.getRoomSize())
                .tags(room.getTags())
                .amenities(room.getAmenities())
                .badges(room.getBadges())
                .status(room.getStatus())
                .createdAt(room.getCreatedAt())
                .updatedAt(room.getUpdatedAt())
                .build();
    }
    
    private RoomDetailResponse toRoomDetailResponse(Room room) {
        return RoomDetailResponse.builder()
                .id(room.getId())
                .title(room.getTitle())
                .description(room.getDescription())
                .pricePerNight(room.getPricePerNight())
                .rating(room.getRating())
                .maxGuests(room.getMaxGuests())
                .bedType(room.getBedType())
                .roomSize(room.getRoomSize())
                .tags(room.getTags())
                .amenities(room.getAmenities())
                .badges(room.getBadges())
                .status(room.getStatus())
                .createdAt(room.getCreatedAt())
                .updatedAt(room.getUpdatedAt())
                .build();
    }
}
