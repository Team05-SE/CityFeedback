package com.example.cityfeedback.usermanagement.domain.model;

import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;

import java.util.Objects;
import java.util.UUID;

/**
 * Aggregate Root: User
 * Repräsentiert einen registrierten Benutzer in unserem City-Feedback-System.
 */
public class User {

    private final UUID id;
    private final Email email;
    private final Password password;
    private final UserRole role;

    private User(UUID id, Email email, Password password, UserRole role) {
        this.id = Objects.requireNonNull(id);
        this.email = Objects.requireNonNull(email);
        this.password = Objects.requireNonNull(password);
        this.role = Objects.requireNonNull(role);
    }

    /**
     * Factory Method für Registrierung eines neuen Bürgers.
     * Rolle wird bewusst in der Domain gesetzt, nicht außerhalb.
     */
    public static User register(Email email, Password password) {
        return new User(UUID.randomUUID(), email, password, UserRole.CITIZEN);
    }

    public UUID getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }
}