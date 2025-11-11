package com.example.cityfeedback.usermanagement.domain.valueobjects;

import java.util.Objects;

public class Password {

    private final String value;

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

    // Hashing kommt später → jetzt erstmal semantisch korrekt
    @Override
    public String toString() {
        return "********"; // Sicherheit: nie Klartext zurückgeben
    }

    @Override
    public boolean equals(Object o) {
        return this == o; // Gleichheit von Passwörtern prüfen wir nicht in Domain
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}