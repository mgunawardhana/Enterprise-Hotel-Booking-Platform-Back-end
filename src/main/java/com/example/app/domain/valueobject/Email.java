package com.example.app.domain.valueobject;

import lombok.Value;

import java.util.regex.Pattern;

/**
 * Value object for Email.
 * Ensures email validity at domain level.
 */
@Value
public class Email {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    String value;
    
    public Email(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        
        String trimmedValue = value.trim().toLowerCase();
        
        if (!EMAIL_PATTERN.matcher(trimmedValue).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + value);
        }
        
        this.value = trimmedValue;
    }
    
    /**
     * Get the domain part of the email
     */
    public String getDomain() {
        return value.substring(value.indexOf('@') + 1);
    }
    
    /**
     * Get the local part of the email
     */
    public String getLocalPart() {
        return value.substring(0, value.indexOf('@'));
    }
    
    @Override
    public String toString() {
        return value;
    }
}
