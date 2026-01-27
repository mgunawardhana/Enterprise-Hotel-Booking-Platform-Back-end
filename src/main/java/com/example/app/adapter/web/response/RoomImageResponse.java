package com.example.app.adapter.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for room image metadata.
 * Does not include binary data for list views (performance optimization).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomImageResponse {
    
    private UUID id;
    private UUID roomId;
    private String filename;
    private String contentType;
    private Long fileSize;
    private boolean isMain;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private String imageUrl; // URL to retrieve the actual image
}
