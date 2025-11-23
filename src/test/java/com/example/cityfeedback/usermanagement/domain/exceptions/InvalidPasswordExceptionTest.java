package com.example.cityfeedback.usermanagement.domain.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-Tests für InvalidPasswordException.
 */
class InvalidPasswordExceptionTest {

    @Test
    void constructorWithoutMessage_shouldSetDefaultMessage() {
        // Act
        InvalidPasswordException exception = new InvalidPasswordException();

        // Assert
        assertNotNull(exception);
        assertEquals("Ungültiges Passwort", exception.getMessage());
    }

    @Test
    void constructorWithMessage_shouldSetMessage() {
        // Arrange
        String message = "Das Passwort ist falsch";

        // Act
        InvalidPasswordException exception = new InvalidPasswordException(message);

        // Assert
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void constructorWithMessageAndCause_shouldSetMessageAndCause() {
        // Arrange
        String message = "Das Passwort ist falsch";
        Throwable cause = new RuntimeException("Root cause");

        // Act
        InvalidPasswordException exception = new InvalidPasswordException(message, cause);

        // Assert
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}

