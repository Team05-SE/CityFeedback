package com.example.cityfeedback.feedbackmanagement.domain.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-Tests für FeedbackNotFoundException.
 */
class FeedbackNotFoundExceptionTest {

    @Test
    void constructorWithFeedbackId_shouldSetMessage() {
        // Arrange
        Long feedbackId = 123L;

        // Act
        FeedbackNotFoundException exception = new FeedbackNotFoundException(feedbackId);

        // Assert
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains(feedbackId.toString()));
        assertTrue(exception.getMessage().contains("nicht gefunden"));
    }

    @Test
    void constructorWithMessage_shouldSetMessage() {
        // Arrange
        String message = "Feedback nicht gefunden";

        // Act
        FeedbackNotFoundException exception = new FeedbackNotFoundException(message);

        // Assert
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void constructorWithMessageAndCause_shouldSetMessageAndCause() {
        // Arrange
        String message = "Feedback nicht gefunden";
        Throwable cause = new RuntimeException("Root cause");

        // Act
        FeedbackNotFoundException exception = new FeedbackNotFoundException(message, cause);

        // Assert
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}

