package com.example.cityfeedback.usermanagement.domain.exceptions;

import java.util.UUID;

/**
 * Wird geworfen, wenn ein Benutzer mit der angegebenen ID nicht gefunden wird.
 */
public class UserNotFoundException extends RuntimeException {
    
    public UserNotFoundException(String message) {
        super(message);
    }
    
    public UserNotFoundException(UUID id) {
        super("Benutzer mit ID " + id + " wurde nicht gefunden.");
    }
}

