package com.example.cityfeedback.usermanagement.domain.exceptions;

/**
 * Wird geworfen, wenn ein Benutzer nicht berechtigt ist, eine bestimmte Aktion auszuführen.
 */
public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException(String message) {
        super(message);
    }
}

