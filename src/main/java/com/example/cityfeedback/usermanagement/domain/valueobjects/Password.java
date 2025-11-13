package com.example.cityfeedback.usermanagement.domain.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Password {

    @Column(name = "password", nullable = false)
    private String value;

    // Für JPA: benötigt!
    protected Password() {}

    public Password(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Passwort darf nicht null sein.");
        }
        if (value.length() < 8) {
            throw new IllegalArgumentException("Passwort muss mindestens 8 Zeichen lang sein.");
        }
        if (!value.chars().anyMatch(Character::isLetter)) {
            throw new IllegalArgumentException("Passwort muss mindestens einen Buchstaben enthalten.");
        }
        if (!value.chars().anyMatch(Character::isDigit)) {
            throw new IllegalArgumentException("Passwort muss mindestens eine Zahl enthalten.");
        }

        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "********"; // Niemals Klartext zurückgeben
    }

    @Override
    public boolean equals(Object o) {
        // Passwort-Vergleich sinnvoll nur anhand des Werts
        if (this == o) return true;
        if (!(o instanceof Password that)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
