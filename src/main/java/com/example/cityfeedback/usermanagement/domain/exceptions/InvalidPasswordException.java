package com.example.cityfeedback.usermanagement.domain.exceptions;

/**
 * Domain Exception: Wird geworfen, wenn ein ungültiges Passwort verwendet wird
 * (z.B. bei Login mit falschem Passwort).
 */
public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException() {
        super("Ungültiges Passwort");
    }

    public InvalidPasswordException(String message) {
        super(message);
    }

    public InvalidPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}

