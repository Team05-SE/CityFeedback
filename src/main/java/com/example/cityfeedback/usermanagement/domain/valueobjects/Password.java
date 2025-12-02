package com.example.cityfeedback.usermanagement.domain.valueobjects;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Value Object für Passwörter.
 * Kapselt Validierung, Hashing und Verifikation von Passwörtern.
 * 
 * Diese Klasse ist framework-unabhängig und enthält keine JPA-Annotationen.
 */
public class Password {

    private String hashed;


    // Registrierung: Hash wird direkt erzeugt
    public Password(String rawPassword) {
        validatePassword(rawPassword);
        this.hashed = hash(rawPassword);
    }

    // Factory-Methode für bereits gehashte Passwörter (z.B. beim Laden aus DB)
    public static Password fromHash(String hashedPassword) {
        if (hashedPassword == null || hashedPassword.isBlank()) {
            throw new IllegalArgumentException("Gehashtes Passwort darf nicht null oder leer sein.");
        }
        Password password = new Password();
        password.hashed = hashedPassword;
        return password;
    }

    // Package-private Konstruktor für fromHash
    private Password() {
        // Für JPA und fromHash
    }

    // Passwort-Anforderungen überprüfen
    private void validatePassword(String value) {
        if (value == null)
            throw new IllegalArgumentException("Passwort darf nicht null sein.");

        if (value.length() < 8)
            throw new IllegalArgumentException("Passwort muss mindestens 8 Zeichen lang sein.");

        if (!value.chars().anyMatch(Character::isLetter))
            throw new IllegalArgumentException("Passwort muss mindestens einen Buchstaben enthalten.");

        if (!value.chars().anyMatch(Character::isDigit))
            throw new IllegalArgumentException("Passwort muss mindestens eine Zahl enthalten.");
    }

    // BCrypt Hash generieren
    private String hash(String rawPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(rawPassword);
    }

    // Getter für JPA & Login-Service
    public String getValue() {
        return hashed;
    }

    // Wird vom Login-Service genutzt
    public boolean matches(String rawPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(rawPassword, this.hashed);
    }

    @Override
    public String toString() {
        return "********"; // Niemals Klartext
    }
}
