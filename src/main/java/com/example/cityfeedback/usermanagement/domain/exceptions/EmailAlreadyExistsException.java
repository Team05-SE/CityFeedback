package com.example.cityfeedback.usermanagement.domain.exceptions;

/**
 * Domain Exception: Wird geworfen, wenn versucht wird, einen User mit einer
 * bereits registrierten E-Mail-Adresse zu registrieren.
 */
public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super("E-Mail-Adresse ist bereits registriert: " + email);
    }

    public EmailAlreadyExistsException(String email, Throwable cause) {
        super("E-Mail-Adresse ist bereits registriert: " + email, cause);
    }
}

