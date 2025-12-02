package com.example.cityfeedback.usermanagement.domain.exceptions;

/**
 * Wird geworfen, wenn versucht wird, einen Benutzer mit einer bereits existierenden E-Mail zu registrieren.
 */
public class EmailAlreadyExistsException extends RuntimeException {
    
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
    
    public EmailAlreadyExistsException(String email, Throwable cause) {
        super("E-Mail-Adresse " + email + " ist bereits registriert.", cause);
    }
}

