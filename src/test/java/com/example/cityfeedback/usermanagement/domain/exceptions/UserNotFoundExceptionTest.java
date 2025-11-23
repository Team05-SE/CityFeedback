package com.example.cityfeedback.usermanagement.domain.exceptions;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-Tests für UserNotFoundException.
 */
class UserNotFoundExceptionTest {

    @Test
    void constructorWithUserId_shouldSetMessage() {
        // Arrange
        UUID userId = UUID.randomUUID();

        // Act
        UserNotFoundException exception = new UserNotFoundException(userId);

        // Assert
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains(userId.toString()));
        assertTrue(exception.getMessage().contains("nicht gefunden"));
    }

    @Test
    void constructorWithEmail_shouldSetMessage() {
        // Arrange
        String email = "test@mail.de";

        // Act
        UserNotFoundException exception = new UserNotFoundException(email);

        // Assert
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains(email));
        assertTrue(exception.getMessage().contains("nicht gefunden"));
    }

    @Test
    void constructorWithMessageAndCause_shouldSetMessageAndCause() {
        // Arrange
        String message = "User nicht gefunden";
        Throwable cause = new RuntimeException("Root cause");

        // Act
        UserNotFoundException exception = new UserNotFoundException(message, cause);

        // Assert
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}

