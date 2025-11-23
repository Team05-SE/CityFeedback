package com.example.cityfeedback.usermanagement.domain.valueobjects;

import com.example.cityfeedback.usermanagement.domain.services.PasswordHasher;

public class Password {

    private final String hashed;

    public Password() {
        // Für Framework-Unabhängigkeit - sollte nicht direkt verwendet werden
        this.hashed = null;
    }

    // Konstruktor für bereits gehashtes Passwort (z.B. aus DB)
    public Password(String hashed) {
        if (hashed == null) {
            throw new IllegalArgumentException("Passwort-Hash darf nicht null sein.");
        }
        this.hashed = hashed;
    }

    // Factory-Methode für neue Passwörter (mit Hashing)
    public static Password create(String rawPassword, PasswordHasher hasher) {
        validatePassword(rawPassword);
        String hashed = hasher.hash(rawPassword);
        return new Password(hashed);
    }

    // Passwort-Anforderungen überprüfen
    public static void validatePassword(String value) {
        if (value == null)
            throw new IllegalArgumentException("Passwort darf nicht null sein.");

        if (value.length() < 8)
            throw new IllegalArgumentException("Passwort muss mindestens 8 Zeichen lang sein.");

        if (!value.chars().anyMatch(Character::isLetter))
            throw new IllegalArgumentException("Passwort muss mindestens einen Buchstaben enthalten.");

        if (!value.chars().anyMatch(Character::isDigit))
            throw new IllegalArgumentException("Passwort muss mindestens eine Zahl enthalten.");
    }

    // Getter für Hash-Wert
    public String getValue() {
        return hashed;
    }

    // Wird vom Login-Service genutzt - benötigt PasswordHasher Service
    public boolean matches(String rawPassword, PasswordHasher hasher) {
        return hasher.matches(rawPassword, this.hashed);
    }

    @Override
    public String toString() {
        return "********"; // Niemals Klartext
    }
}
