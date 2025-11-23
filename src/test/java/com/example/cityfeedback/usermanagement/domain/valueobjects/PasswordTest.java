package com.example.cityfeedback.usermanagement.domain.valueobjects;

import com.example.cityfeedback.usermanagement.domain.services.PasswordHasher;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {

    private final PasswordHasher mockHasher = new PasswordHasher() {
        @Override
        public String hash(String rawPassword) {
            return "hashed_" + rawPassword;
        }

        @Override
        public boolean matches(String rawPassword, String hashedPassword) {
            return hashedPassword.equals("hashed_" + rawPassword);
        }
    };

    @Test
    void validPassword_shouldBeAccepted() {
        assertDoesNotThrow(() -> Password.create("Abcdef12", mockHasher));
    }

    @Test
    void passwordTooShort_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> Password.create("A1b2c", mockHasher));
    }

    @Test
    void passwordWithoutLetters_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> Password.create("12345678", mockHasher));
    }

    @Test
    void passwordWithoutDigits_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> Password.create("ABCDEFGH", mockHasher));
    }

    @Test
    void passwordWithExactlyEightCharacters_shouldBeValid() {
        assertDoesNotThrow(() -> Password.create("A1b2c3d4", mockHasher));
    }

    @Test
    void passwordWithOnlySpecialCharacters_shouldThrow() {
        assertThrows(IllegalArgumentException.class,
                () -> Password.create("!!!!????", mockHasher));
    }

    @Test
    void passwordWithGermanUmlauts_shouldBeValid() {
        assertDoesNotThrow(() -> Password.create("Äbcdef12", mockHasher));
    }

    @Test
    void createPasswordWithNull_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> Password.create(null, mockHasher));
    }

    @Test
    void passwordFromHash_shouldWork() {
        Password password = new Password("hashed_value");
        assertEquals("hashed_value", password.getValue());
    }
}