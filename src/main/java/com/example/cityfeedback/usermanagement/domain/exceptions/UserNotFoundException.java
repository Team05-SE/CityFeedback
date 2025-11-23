package com.example.cityfeedback.usermanagement.domain.exceptions;

import java.util.UUID;

/**
 * Domain Exception: Wird geworfen, wenn ein User nicht gefunden wird.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(UUID userId) {
        super("User nicht gefunden: " + userId);
    }

    public UserNotFoundException(String email) {
        super("User nicht gefunden mit E-Mail: " + email);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

