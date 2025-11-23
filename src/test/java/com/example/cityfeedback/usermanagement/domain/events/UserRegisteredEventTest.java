package com.example.cityfeedback.usermanagement.domain.events;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-Tests für UserRegisteredEvent Domain Event.
 * Keine Spring-Abhängigkeiten - reine Domain-Logik-Tests.
 */
class UserRegisteredEventTest {

    @Test
    void constructor_shouldSetAllFields() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String email = "test@mail.de";

        // Act
        UserRegisteredEvent event = new UserRegisteredEvent(userId, email);

        // Assert
        assertEquals(userId, event.getUserId());
        assertEquals(email, event.getEmail());
        assertNotNull(event.getOccurredOn());
    }

    @Test
    void constructor_shouldSetOccurredOnTimestamp() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String email = "test@mail.de";
        Instant beforeCreation = Instant.now();

        // Act
        UserRegisteredEvent event = new UserRegisteredEvent(userId, email);
        Instant afterCreation = Instant.now();

        // Assert
        assertNotNull(event.getOccurredOn());
        assertTrue(event.getOccurredOn().isAfter(beforeCreation.minusSeconds(1)));
        assertTrue(event.getOccurredOn().isBefore(afterCreation.plusSeconds(1)));
    }

    @Test
    void constructor_shouldAcceptValidEmail() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String email = "user@example.com";

        // Act
        UserRegisteredEvent event = new UserRegisteredEvent(userId, email);

        // Assert
        assertEquals(email, event.getEmail());
    }

    @Test
    void constructor_shouldAcceptValidUserId() {
        // Arrange
        UUID userId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        String email = "test@mail.de";

        // Act
        UserRegisteredEvent event = new UserRegisteredEvent(userId, email);

        // Assert
        assertEquals(userId, event.getUserId());
    }

    @Test
    void toString_shouldContainRelevantInformation() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String email = "test@mail.de";
        UserRegisteredEvent event = new UserRegisteredEvent(userId, email);

        // Act
        String result = event.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("UserRegisteredEvent"));
        assertTrue(result.contains(userId.toString()));
        assertTrue(result.contains(email));
    }

    @Test
    void getOccurredOn_shouldReturnNonNullInstant() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String email = "test@mail.de";

        // Act
        UserRegisteredEvent event = new UserRegisteredEvent(userId, email);

        // Assert
        assertNotNull(event.getOccurredOn());
        assertInstanceOf(Instant.class, event.getOccurredOn());
    }
}

