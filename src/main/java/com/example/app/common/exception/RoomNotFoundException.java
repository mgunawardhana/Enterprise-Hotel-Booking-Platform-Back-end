package com.example.app.common.exception;

/**
 * Exception thrown when a room is not found.
 */
public class RoomNotFoundException extends RuntimeException {
    
    public RoomNotFoundException(String message) {
        super(message);
    }
    
    public RoomNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
