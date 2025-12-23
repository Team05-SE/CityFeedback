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
 * 
 * Diese Klasse ist framework-unabhängig und enthält keine JPA-Annotationen.
 * Die Persistierung wird über UserEntity und UserMapper in der Infrastructure-Schicht gehandhabt.
 */
public class User {

    private UUID id;

    private Email email;
    private Password password;
    private UserRole role;

    // Domain Events werden nicht persistiert
    private final List<Object> domainEvents = new ArrayList<>();

    /**
     * No-Args Konstruktor für Mapper in Infrastructure.
     * WARNUNG: Verwende stattdessen die Factory-Methode register() oder den öffentlichen Konstruktor.
     */
    public User() {}

    public User(Email email, Password password, UserRole role) {
        this.email = Objects.requireNonNull(email);
        this.password = Objects.requireNonNull(password);
        this.role = Objects.requireNonNull(role);
    }

    public static User register(Email email, Password password) {
        User user = new User(email, password, UserRole.CITIZEN);
        user.registerEvent(new UserRegisteredEvent(user.id, email.getValue()));
        return user;
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

    /**
     * Setter für Mapper in Infrastructure.
     * WARNUNG: Diese Methode ist nur für die Persistierung gedacht.
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Ändert das Passwort des Users.
     * 
     * @param newPassword Das neue Passwort
     * @throws IllegalArgumentException wenn das neue Passwort null ist
     */
    public void changePassword(Password newPassword) {
        this.password = Objects.requireNonNull(newPassword, "Passwort darf nicht null sein");
    }

    /**
     * Ändert die Rolle des Users.
     * 
     * @param newRole Die neue Rolle
     * @throws IllegalArgumentException wenn die neue Rolle null ist
     */
    public void changeRole(UserRole newRole) {
        this.role = Objects.requireNonNull(newRole, "Rolle darf nicht null sein");
    }
}