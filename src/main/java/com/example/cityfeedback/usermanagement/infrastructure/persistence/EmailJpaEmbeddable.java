package com.example.cityfeedback.usermanagement.infrastructure.persistence;

import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * JPA-Embeddable für Email Value Object.
 */
@Embeddable
public class EmailJpaEmbeddable {

    @Column(name = "email", nullable = false, unique = true)
    private String value;

    protected EmailJpaEmbeddable() {
        // JPA benötigt einen geschützten No-Args Konstruktor
    }

    public EmailJpaEmbeddable(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Email toDomain() {
        if (value == null) {
            return null;
        }
        return new Email(value);
    }

    public static EmailJpaEmbeddable fromDomain(Email email) {
        return new EmailJpaEmbeddable(email.getValue());
    }
}

