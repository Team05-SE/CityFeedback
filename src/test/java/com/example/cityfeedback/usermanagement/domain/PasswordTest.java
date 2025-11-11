package com.example.cityfeedback.usermanagement.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {

    @Test
    void validPassword_shouldBeAccepted() {
        assertDoesNotThrow(() -> new Password("Abcdef12"));
    }

    @Test
    void passwordTooShort_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> new Password("A1b2c"));
    }

    @Test
    void passwordWithoutLetters_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> new Password("12345678"));
    }

    @Test
    void passwordWithoutDigits_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> new Password("ABCDEFGH"));
    }

    @Test
    void passwordWithExactlyEightCharacters_shouldBeValid() {
        assertDoesNotThrow(() -> new Password("A1b2c3d4"));
    }

    @Test
    void passwordWithOnlySpecialCharacters_shouldThrow() {
        assertThrows(IllegalArgumentException.class,
                () -> new Password("!!!!????"));
    }

    @Test
    void passwordWithGermanUmlauts_shouldBeValid() {
        assertDoesNotThrow(() -> new Password("Äbcdef12"));
    }
}