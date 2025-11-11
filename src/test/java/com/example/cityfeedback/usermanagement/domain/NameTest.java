package com.example.cityfeedback.usermanagement.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NameTest {

    // Happy Path Test
    @Test
    void validName() {
        assertDoesNotThrow(() -> new Name("Peter"));
    }

    // Happy Path Test
    @Test
    void oneCharacterIsValid() {
        assertDoesNotThrow(() -> new Name("A"));
    }

    // Edge Case Test
    @Test
    void nameWithSpacesAroundIsValid() {
        Name name = new Name("  Anna  ");
        assertEquals("  Anna  ", name.getValue());
    }

    // Negative Test
    @Test
    void nameIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Name(null));
    }

    // Negative Test
    @Test
    void nameIsEmpty() {
        assertThrows(IllegalArgumentException.class,
                () -> new Name(""));
    }

    // Negative Test
    @Test
    void nameOnlyWhitespace() {
        assertThrows(IllegalArgumentException.class,
                () -> new Name("   "));
    }
}
