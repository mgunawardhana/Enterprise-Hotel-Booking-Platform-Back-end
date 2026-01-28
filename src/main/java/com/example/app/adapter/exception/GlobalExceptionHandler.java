package com.example.app.adapter.exception;

import com.example.app.common.exception.DuplicateResourceException;
import com.example.app.common.exception.InvalidSearchCriteriaException;
import com.example.app.common.exception.InvalidTokenException;
import com.example.app.common.exception.ResourceNotFoundException;
import com.example.app.common.response.CommonResponse;
import com.example.app.common.util.TraceIdGenerator;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for all REST controllers
 * Returns CommonResponse for all exceptions
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Handle authentication exceptions (401)
     */
    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
    public ResponseEntity<CommonResponse<Void>> handleAuthenticationException(AuthenticationException ex) {
        String traceId = TraceIdGenerator.generate();
        log.error("[{}] Authentication error: {}", traceId, ex.getMessage());
        
        CommonResponse<Void> response = CommonResponse.error(
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                traceId
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
    /**
     * Handle access denied exceptions (403)
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CommonResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        String traceId = TraceIdGenerator.generate();
        log.error("[{}] Access denied: {}", traceId, ex.getMessage());
        
        CommonResponse<Void> response = CommonResponse.error(
                "Access denied: " + ex.getMessage(),
                HttpStatus.FORBIDDEN.value(),
                traceId
        );
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
    
    /**
     * Handle resource not found exceptions (404)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CommonResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        String traceId = TraceIdGenerator.generate();
        log.error("[{}] Resource not found: {}", traceId, ex.getMessage());
        
        CommonResponse<Void> response = CommonResponse.error(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                traceId
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    /**
     * Handle duplicate resource exceptions (409)
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<CommonResponse<Void>> handleDuplicateResourceException(DuplicateResourceException ex) {
        String traceId = TraceIdGenerator.generate();
        log.error("[{}] Duplicate resource: {}", traceId, ex.getMessage());
        
        CommonResponse<Void> response = CommonResponse.error(
                ex.getMessage(),
                HttpStatus.CONFLICT.value(),
                traceId
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    
    /**
     * Handle validation exceptions (422)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex) {
        String traceId = TraceIdGenerator.generate();
        log.error("[{}] Validation error: {}", traceId, ex.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        CommonResponse<Map<String, String>> response = CommonResponse.<Map<String, String>>builder()
                .success(false)
                .message("Validation failed")
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .data(errors)
                .traceId(traceId)
                .build();
        
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }
    
    /**
     * Handle JWT exceptions (401)
     */
    @ExceptionHandler({JwtException.class, InvalidTokenException.class})
    public ResponseEntity<CommonResponse<Void>> handleJwtException(Exception ex) {
        String traceId = TraceIdGenerator.generate();
        log.error("[{}] JWT error: {}", traceId, ex.getMessage());
        
        CommonResponse<Void> response = CommonResponse.error(
                "Invalid or expired token: " + ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                traceId
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
    /**
     * Handle OAuth2 authentication exceptions (401)
     */
    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseEntity<CommonResponse<Void>> handleOAuth2AuthenticationException(OAuth2AuthenticationException ex) {
        String traceId = TraceIdGenerator.generate();
        log.error("[{}] OAuth2 authentication error: {}", traceId, ex.getMessage());
        
        CommonResponse<Void> response = CommonResponse.error(
                "OAuth2 authentication failed: " + ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                traceId
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
    /**
     * Handle data integrity violations (409)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CommonResponse<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String traceId = TraceIdGenerator.generate();
        log.error("[{}] Data integrity violation: {}", traceId, ex.getMessage());
        
        CommonResponse<Void> response = CommonResponse.error(
                "Data integrity violation: duplicate or invalid data",
                HttpStatus.CONFLICT.value(),
                traceId
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    
    /**
     * Handle illegal argument exceptions (400)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        String traceId = TraceIdGenerator.generate();
        log.error("[{}] Illegal argument: {}", traceId, ex.getMessage());
        
        CommonResponse<Void> response = CommonResponse.error(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                traceId
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * Handle invalid search criteria exceptions (400)
     */
    @ExceptionHandler(InvalidSearchCriteriaException.class)
    public ResponseEntity<CommonResponse<Void>> handleInvalidSearchCriteriaException(InvalidSearchCriteriaException ex) {
        String traceId = TraceIdGenerator.generate();
        log.error("[{}] Invalid search criteria: {}", traceId, ex.getMessage());
        
        CommonResponse<Void> response = CommonResponse.error(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                traceId
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * Handle room not found exceptions (404)
     */
    @ExceptionHandler(com.example.app.common.exception.RoomNotFoundException.class)
    public ResponseEntity<CommonResponse<Void>> handleRoomNotFoundException(
            com.example.app.common.exception.RoomNotFoundException ex) {
        String traceId = TraceIdGenerator.generate();
        log.error("[{}] Room not found: {}", traceId, ex.getMessage());
        
        CommonResponse<Void> response = CommonResponse.error(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                traceId
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    /**
     * Handle image processing exceptions (422)
     */
    @ExceptionHandler(com.example.app.common.exception.ImageProcessingException.class)
    public ResponseEntity<CommonResponse<Void>> handleImageProcessingException(
            com.example.app.common.exception.ImageProcessingException ex) {
        String traceId = TraceIdGenerator.generate();
        log.error("[{}] Image processing error: {}", traceId, ex.getMessage());
        
        CommonResponse<Void> response = CommonResponse.error(
                ex.getMessage(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                traceId
        );
        
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }
    
    /**
     * Handle multipart file upload exceptions (413)
     */
    @ExceptionHandler(org.springframework.web.multipart.MultipartException.class)
    public ResponseEntity<CommonResponse<Void>> handleMultipartException(
            org.springframework.web.multipart.MultipartException ex) {
        String traceId = TraceIdGenerator.generate();
        log.error("[{}] Multipart upload error: {}", traceId, ex.getMessage());
        
        CommonResponse<Void> response = CommonResponse.error(
                "File upload failed: " + ex.getMessage(),
                HttpStatus.PAYLOAD_TOO_LARGE.value(),
                traceId
        );
        
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(response);
    }
    
    /**
     * Handle max upload size exceeded exceptions (413)
     */
    @ExceptionHandler(org.springframework.web.multipart.MaxUploadSizeExceededException.class)
    public ResponseEntity<CommonResponse<Void>> handleMaxUploadSizeExceededException(
            org.springframework.web.multipart.MaxUploadSizeExceededException ex) {
        String traceId = TraceIdGenerator.generate();
        log.error("[{}] File size exceeded: {}", traceId, ex.getMessage());
        
        CommonResponse<Void> response = CommonResponse.error(
                "File size exceeds maximum allowed size",
                HttpStatus.PAYLOAD_TOO_LARGE.value(),
                traceId
        );
        
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(response);
    }
    
    /**
     * Handle all other exceptions (500)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Void>> handleGenericException(Exception ex) {
        String traceId = TraceIdGenerator.generate();
        log.error("[{}] Unexpected error: {}", traceId, ex.getMessage(), ex);
        
        CommonResponse<Void> response = CommonResponse.error(
                "An unexpected error occurred. Please contact support with trace ID: " + traceId,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                traceId
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
