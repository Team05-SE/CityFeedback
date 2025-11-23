package com.example.cityfeedback.usermanagement.infrastructure.persistence;

import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * JPA-Embeddable für Password Value Object.
 */
@Embeddable
public class PasswordJpaEmbeddable {

    @Column(name = "password", nullable = false)
    private String hashed;

    protected PasswordJpaEmbeddable() {
        // Für JPA
    }

    public PasswordJpaEmbeddable(String hashed) {
        this.hashed = hashed;
    }

    public String getHashed() {
        return hashed;
    }

    public void setHashed(String hashed) {
        this.hashed = hashed;
    }

    public Password toDomain() {
        if (hashed == null) {
            return null;
        }
        return new Password(hashed);
    }

    public static PasswordJpaEmbeddable fromDomain(Password password) {
        return new PasswordJpaEmbeddable(password.getValue());
    }
}

