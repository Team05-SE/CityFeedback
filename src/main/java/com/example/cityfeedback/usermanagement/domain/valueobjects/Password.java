package com.example.cityfeedback.usermanagement.domain.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Embeddable
public class Password {

    @Column(name = "password", nullable = false)
    private String hashed;

    protected Password() {
        // Für JPA
    }

    // Registrierung: Hash wird direkt erzeugt
    public Password(String rawPassword) {
        validatePassword(rawPassword);
        this.hashed = hash(rawPassword);
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
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    // Getter für JPA & Login-Service
    public String getValue() {
        return hashed;
    }

    // Wird vom Login-Service genutzt
    public boolean matches(String rawPassword) {
        return BCrypt.checkpw(rawPassword, this.hashed);
    }

    @Override
    public String toString() {
        return "********"; // Niemals Klartext
    }
}
