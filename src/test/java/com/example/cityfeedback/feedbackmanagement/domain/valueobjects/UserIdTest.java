package com.example.cityfeedback.feedbackmanagement.domain.valueobjects;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-Tests für UserId Value Object.
 * Keine Spring-Abhängigkeiten - reine Domain-Logik-Tests.
 */
class UserIdTest {

    @Test
    void constructor_withUUID_shouldWork() {
        // Arrange
        UUID uuid = UUID.randomUUID();

        // Act
        UserId userId = new UserId(uuid);

        // Assert
        assertEquals(uuid, userId.getValue());
    }

    @Test
    void constructor_withString_shouldWork() {
        // Arrange
        String uuidString = UUID.randomUUID().toString();

        // Act
        UserId userId = new UserId(uuidString);

        // Assert
        assertEquals(uuidString, userId.getValue().toString());
    }

    @Test
    void constructor_withNullUUID_shouldThrow() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new UserId((UUID) null);
        });
    }

    @Test
    void constructor_withNullString_shouldThrow() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new UserId((String) null);
        });
    }

    @Test
    void constructor_withInvalidString_shouldThrow() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new UserId("invalid-uuid");
        });
    }

    @Test
    void equals_shouldWork() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        UserId userId1 = new UserId(uuid);
        UserId userId2 = new UserId(uuid);

        // Act & Assert
        assertEquals(userId1, userId2);
    }

    @Test
    void hashCode_shouldWork() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        UserId userId1 = new UserId(uuid);
        UserId userId2 = new UserId(uuid);

        // Act & Assert
        assertEquals(userId1.hashCode(), userId2.hashCode());
    }
}

