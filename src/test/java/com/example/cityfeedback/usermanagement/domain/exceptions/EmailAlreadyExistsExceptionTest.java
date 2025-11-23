package com.example.cityfeedback.usermanagement.domain.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-Tests für EmailAlreadyExistsException.
 */
class EmailAlreadyExistsExceptionTest {

    @Test
    void constructorWithEmail_shouldSetMessage() {
        // Arrange
        String email = "test@mail.de";

        // Act
        EmailAlreadyExistsException exception = new EmailAlreadyExistsException(email);

        // Assert
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains(email));
        assertTrue(exception.getMessage().contains("bereits registriert"));
    }

    @Test
    void constructorWithEmailAndCause_shouldSetMessageAndCause() {
        // Arrange
        String email = "test@mail.de";
        Throwable cause = new RuntimeException("Root cause");

        // Act
        EmailAlreadyExistsException exception = new EmailAlreadyExistsException(email, cause);

        // Assert
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains(email));
        assertEquals(cause, exception.getCause());
    }
}

