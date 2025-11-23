package com.example.cityfeedback.usermanagement.infrastructure.persistence;

import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import jakarta.persistence.*;

import java.util.UUID;

/**
 * JPA-Entity für User.
 * Mapping zwischen Domain-Entity und Datenbank.
 */
@Entity
@Table(name = "users")
public class UserJpaEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Embedded
    private EmailJpaEmbeddable email;

    @Embedded
    private PasswordJpaEmbeddable password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public UserJpaEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public EmailJpaEmbeddable getEmail() {
        return email;
    }

    public void setEmail(EmailJpaEmbeddable email) {
        this.email = email;
    }

    public PasswordJpaEmbeddable getPassword() {
        return password;
    }

    public void setPassword(PasswordJpaEmbeddable password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}

