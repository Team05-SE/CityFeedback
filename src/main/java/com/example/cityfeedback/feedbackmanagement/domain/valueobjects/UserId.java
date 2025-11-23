package com.example.cityfeedback.feedbackmanagement.domain.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object für User-ID im Feedbackmanagement-Context.
 * Entkoppelt Feedback vom usermanagement-Bounded Context.
 */
public class UserId {

    private final UUID value;

    @JsonCreator
    public UserId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("User-ID darf nicht null sein");
        }
        this.value = value;
    }

    public UserId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("User-ID darf nicht null oder leer sein");
        }
        try {
            this.value = UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Ungültige User-ID: " + value, e);
        }
    }

    @JsonValue
    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserId userId)) return false;
        return Objects.equals(value, userId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

