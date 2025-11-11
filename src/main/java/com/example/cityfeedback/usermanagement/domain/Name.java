package com.example.cityfeedback.usermanagement.domain;

import java.util.Objects;

public class Name {

    private final String value;

    public Name(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Name darf nicht null sein.");
        }
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException("Name darf nicht leer sein.");
        }

        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value; // Namen dürfen angezeigt werden
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Name other)) return false;
        return Objects.equals(this.value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

