package com.example.cityfeedback.usermanagement.domain.exceptions;

/**
 * Wird geworfen, wenn die Anmeldedaten (E-Mail oder Passwort) ungültig sind.
 */
public class InvalidCredentialsException extends RuntimeException {
    
    public InvalidCredentialsException(String message) {
        super(message);
    }
    
    public InvalidCredentialsException() {
        super("Ungültige Anmeldedaten.");
    }
}

