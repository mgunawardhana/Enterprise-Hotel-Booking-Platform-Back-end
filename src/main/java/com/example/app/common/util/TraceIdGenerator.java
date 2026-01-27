package com.example.app.common.util;

import java.util.UUID;

/**
 * Utility class to generate unique trace IDs for request tracking
 */
public class TraceIdGenerator {
    
    /**
     * Generate a unique trace ID
     */
    public static String generate() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
    
    /**
     * Generate trace ID with prefix
     */
    public static String generateWithPrefix(String prefix) {
        return prefix + "-" + generate();
    }
}
