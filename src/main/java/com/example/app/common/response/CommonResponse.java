package com.example.app.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Standard API response wrapper for ALL endpoints.
 * Ensures consistent response structure across the application.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> {
    
    private boolean success;
    private String message;
    private int statusCode;
    private T data;
    
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    private String traceId;
    
    /**
     * Create success response with data
     */
    public static <T> CommonResponse<T> success(T data, String message, int statusCode, String traceId) {
        return CommonResponse.<T>builder()
                .success(true)
                .message(message)
                .statusCode(statusCode)
                .data(data)
                .timestamp(LocalDateTime.now())
                .traceId(traceId)
                .build();
    }
    
    /**
     * Create success response with default message
     */
    public static <T> CommonResponse<T> success(T data, String traceId) {
        return success(data, "Operation successful", 200, traceId);
    }
    
    /**
     * Create error response
     */
    public static <T> CommonResponse<T> error(String message, int statusCode, String traceId) {
        return CommonResponse.<T>builder()
                .success(false)
                .message(message)
                .statusCode(statusCode)
                .data(null)
                .timestamp(LocalDateTime.now())
                .traceId(traceId)
                .build();
    }
}
