package com.example.app.common.exception;

/**
 * Exception thrown when search criteria validation fails.
 */
public class InvalidSearchCriteriaException extends RuntimeException {
    
    public InvalidSearchCriteriaException(String message) {
        super(message);
    }
    
    public InvalidSearchCriteriaException(String message, Throwable cause) {
        super(message, cause);
    }
}
