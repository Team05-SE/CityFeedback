package com.example.cityfeedback.usermanagement.infrastructure.persistence;

import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import jakarta.persistence.*;

import java.util.UUID;

/**
 * JPA Entity für User.
 * Diese Klasse ist nur für die Persistierung zuständig.
 * Die Domain-Logik befindet sich in der User-Klasse im Domain-Layer.
 */
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    // JPA benötigt einen No-Args Konstruktor
    protected UserEntity() {
    }

    public UserEntity(UUID id, String email, String password, UserRole role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}

