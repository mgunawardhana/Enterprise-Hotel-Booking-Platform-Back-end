package com.example.app.infrastructure.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Service for handling image storage operations.
 * Validates and processes multipart file uploads.
 */
@Slf4j
@Service
public class ImageStorageService {
    
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/webp"
    );
    
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    
    /**
     * Validate image file
     */
    public void validateImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size of 10MB");
        }
        
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Invalid file type. Only JPEG, PNG, and WebP are allowed");
        }
    }
    
    /**
     * Convert MultipartFile to byte array
     */
    public byte[] convertToByteArray(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            log.error("Error converting file to byte array: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process image file", e);
        }
    }
    
    /**
     * Generate unique filename
     */
    public String generateUniqueFilename(String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }
}
