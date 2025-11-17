package com.example.cityfeedback.usermanagement.domain.model;

import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

/**
 * Aggregate Root: User
 * Repräsentiert einen registrierten Benutzer in unserem City-Feedback-System.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public User() {

    }

    public User(Email email, Password password, UserRole role) {
        this.email = Objects.requireNonNull(email);
        this.password = Objects.requireNonNull(password);
        this.role = Objects.requireNonNull(role);
    }

    public static User register(Email email, Password password) {
        return new User(email, password, UserRole.CITIZEN);
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
