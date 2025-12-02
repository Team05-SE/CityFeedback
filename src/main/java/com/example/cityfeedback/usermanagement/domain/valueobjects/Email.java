package com.example.cityfeedback.usermanagement.domain.valueobjects;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object für E-Mail-Adressen.
 * Kapselt Validierung und Normalisierung von E-Mail-Adressen.
 * 
 * Diese Klasse ist framework-unabhängig und enthält keine JPA-Annotationen.
 */
public class Email {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final String value;

    public Email(String value) {
        if (value == null) {
            throw new IllegalArgumentException("E-Mail darf nicht null sein");
        }

        String normalized = value.strip().toLowerCase();

        if (!EMAIL_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Ungültige E-Mail-Adresse: " + value);
        }

        this.value = normalized;
    }

    public String getValue() {
        return value;
    }

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
