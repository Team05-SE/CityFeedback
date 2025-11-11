package com.example.cityfeedback.usermanagement.domain.valueobjects;

import java.util.Objects;
import java.util.regex.Pattern;

public class Email {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final String value;

    public Email(String value) {
        if (value == null) {
            throw new IllegalArgumentException("E-Mail darf nicht null sein");
        }

        // 1. Normalisieren (Leerzeichen entfernen, Kleinschreibung)
        String normalized = value.strip().toLowerCase();

        // 2. Jetzt validieren — WICHTIG: matcher(normalized)
        if (!EMAIL_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Ungültige E-Mail-Adresse: " + value);
        }

        // 3. Speichern
        this.value = normalized;
    }

    public String getValue() {
        return value;
    }

    // Für Entity-Vergleich & HashMap Keys
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email email)) return false;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}