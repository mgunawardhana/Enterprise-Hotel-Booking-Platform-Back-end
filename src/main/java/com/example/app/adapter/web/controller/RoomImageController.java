package com.example.app.adapter.web.controller;

import com.example.app.adapter.web.response.RoomImageResponse;
import com.example.app.common.exception.ImageProcessingException;
import com.example.app.common.response.CommonResponse;
import com.example.app.common.util.TraceIdGenerator;
import com.example.app.domain.model.RoomImage;
import com.example.app.domain.port.in.RoomManagementUseCase;
import com.example.app.infrastructure.storage.ImageStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST controller for room image management.
 * Handles image upload and retrieval.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
@Tag(name = "Room Images", description = "APIs for managing room images")
public class RoomImageController {
    
    private final RoomManagementUseCase roomManagementUseCase;
    private final ImageStorageService imageStorageService;
    
    /**
     * ADMIN: Upload images for a room
     * Future: Add @PreAuthorize("hasRole('ADMIN')")
     */
    @PostMapping("/{roomId}/images")
    @Operation(summary = "Upload room images", description = "Upload one or more images for a room (Admin only)")
    public ResponseEntity<CommonResponse<List<RoomImageResponse>>> uploadRoomImages(
            @PathVariable UUID roomId,
            @RequestParam("images") MultipartFile[] files,
            @RequestParam(value = "mainImageIndex", defaultValue = "0") int mainImageIndex
    ) {
        String traceId = TraceIdGenerator.generate();
        log.info("[{}] POST /api/v1/rooms/{}/images - uploading {} images", traceId, roomId, files.length);
        
        try {
            List<RoomImage> roomImages = new ArrayList<>();
            
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                
                // Validate image
                imageStorageService.validateImage(file);
                
                // Convert to domain model
                RoomImage roomImage = new RoomImage();
                roomImage.setRoomId(roomId);
                roomImage.setFilename(imageStorageService.generateUniqueFilename(file.getOriginalFilename()));
                roomImage.setContentType(file.getContentType());
                roomImage.setFileSize(file.getSize());
                roomImage.setImageData(imageStorageService.convertToByteArray(file));
                roomImage.setMain(i == mainImageIndex);
                roomImage.setDisplayOrder(i);
                
                roomImages.add(roomImage);
            }
            
            // Save images
            List<RoomImage> savedImages = roomManagementUseCase.uploadRoomImages(roomId, roomImages);
            
            // Convert to response
            List<RoomImageResponse> responses = savedImages.stream()
                    .map(image -> toRoomImageResponse(image, roomId))
                    .collect(Collectors.toList());
            
            log.info("[{}] Successfully uploaded {} images for room: {}", traceId, savedImages.size(), roomId);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(
                    responses,
                    "Images uploaded successfully",
                    HttpStatus.CREATED.value(),
                    traceId
            ));
            
        } catch (IllegalArgumentException e) {
            log.error("[{}] Validation error: {}", traceId, e.getMessage());
            throw new ImageProcessingException(e.getMessage(), e);
        } catch (Exception e) {
            log.error("[{}] Error uploading images: {}", traceId, e.getMessage(), e);
            throw new ImageProcessingException("Failed to upload images", e);
        }
    }
    
    /**
     * PUBLIC: Get all images for a room (metadata only)
     */
    @GetMapping("/{roomId}/images")
    @Operation(summary = "Get room images", description = "Retrieve all image metadata for a room")
    public ResponseEntity<CommonResponse<List<RoomImageResponse>>> getRoomImages(@PathVariable UUID roomId) {
        String traceId = TraceIdGenerator.generate();
        log.info("[{}] GET /api/v1/rooms/{}/images", traceId, roomId);
        
        List<RoomImage> images = roomManagementUseCase.getRoomImages(roomId);
        
        List<RoomImageResponse> responses = images.stream()
                .map(image -> toRoomImageResponse(image, roomId))
                .collect(Collectors.toList());
        
        log.info("[{}] Successfully fetched {} images for room: {}", traceId, images.size(), roomId);
        
        return ResponseEntity.ok(CommonResponse.success(
                responses,
                "Images fetched successfully",
                HttpStatus.OK.value(),
                traceId
        ));
    }
    
    /**
     * PUBLIC: Get single image binary data
     */
    @GetMapping("/{roomId}/images/{imageId}")
    @Operation(summary = "Get image binary", description = "Retrieve the actual image binary data")
    public ResponseEntity<byte[]> getImageBinary(
            @PathVariable UUID roomId,
            @PathVariable UUID imageId
    ) {
        String traceId = TraceIdGenerator.generate();
        log.info("[{}] GET /api/v1/rooms/{}/images/{} - fetching binary data", traceId, roomId, imageId);
        
        RoomImage image = roomManagementUseCase.getRoomImageById(imageId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(image.getContentType()));
        headers.setContentLength(image.getFileSize());
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getFilename() + "\"");
        
        log.info("[{}] Successfully fetched image binary: {}", traceId, imageId);
        
        return new ResponseEntity<>(image.getImageData(), headers, HttpStatus.OK);
    }
    
    // Mapping method
    
    private RoomImageResponse toRoomImageResponse(RoomImage image, UUID roomId) {
        return RoomImageResponse.builder()
                .id(image.getId())
                .roomId(roomId)
                .filename(image.getFilename())
                .contentType(image.getContentType())
                .fileSize(image.getFileSize())
                .isMain(image.isMain())
                .displayOrder(image.getDisplayOrder())
                .createdAt(image.getCreatedAt())
                .imageUrl("/api/v1/rooms/" + roomId + "/images/" + image.getId())
                .build();
    }
}
