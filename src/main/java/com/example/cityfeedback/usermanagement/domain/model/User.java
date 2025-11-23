package com.example.cityfeedback.usermanagement.domain.model;

import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import com.example.cityfeedback.usermanagement.domain.events.UserRegisteredEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Aggregate Root: User
 * Repräsentiert einen registrierten Benutzer in unserem City-Feedback-System.
 * Framework-unabhängige Domain-Entität.
 */
public class User {

    private UUID id;

    private Email email;
    private Password password;
    private UserRole role;
    private final List<Object> domainEvents = new ArrayList<>();

    public User() {
        // Für Framework-Unabhängigkeit
    }

    public User(Email email, Password password, UserRole role) {
        this.email = Objects.requireNonNull(email);
        this.password = Objects.requireNonNull(password);
        this.role = Objects.requireNonNull(role);
    }

    public static User register(Email email, Password password) {
        User user = new User(email, password, UserRole.CITIZEN);
        // ID wird beim Speichern im Repository gesetzt
        // Temporäre ID für Domain Event (wird später durch echte ID ersetzt)
        UUID tempId = UUID.randomUUID();
        user.registerEvent(new UserRegisteredEvent(tempId, email.getValue()));
        return user;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }

    private void registerEvent(Object event) {
        this.domainEvents.add(event);
    }

    public List<Object> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        this.domainEvents.clear();
    }

    public UUID getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }
}