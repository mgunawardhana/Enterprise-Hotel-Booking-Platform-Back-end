package com.example.app.common.exception;

/**
 * Exception thrown when image processing fails.
 */
public class ImageProcessingException extends RuntimeException {
    
    public ImageProcessingException(String message) {
        super(message);
    }
    
    public ImageProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
