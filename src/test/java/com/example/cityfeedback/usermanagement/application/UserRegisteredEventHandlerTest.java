package com.example.cityfeedback.usermanagement.application;

import com.example.cityfeedback.usermanagement.domain.events.UserRegisteredEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration-Tests für UserRegisteredEventHandler.
 * Testet die Event-Verarbeitung mit Spring-Kontext.
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.datasource.url=jdbc:h2:mem:testdb"
})
class UserRegisteredEventHandlerTest {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private UserRegisteredEventHandler eventHandler;

    @Test
    void handleUserRegistered_shouldBeInvokedWhenEventIsPublished() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String email = "test@mail.de";
        UserRegisteredEvent event = new UserRegisteredEvent(userId, email);

        // Act & Assert - sollte keine Exception werfen
        assertDoesNotThrow(() -> {
            eventPublisher.publishEvent(event);
            
            // Wait a bit for async processing (if any)
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Handler sollte das Event ohne Fehler verarbeiten können");
    }

    @Test
    void handleUserRegistered_shouldProcessEventWithValidData() {
        // Arrange
        UUID userId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        String email = "newuser@example.com";
        UserRegisteredEvent event = new UserRegisteredEvent(userId, email);

        // Act & Assert - sollte keine Exception werfen
        assertDoesNotThrow(() -> {
            eventPublisher.publishEvent(event);
            
            // Wait for processing
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Handler sollte das Event ohne Fehler verarbeiten können");
    }

    @Test
    void eventHandler_shouldBeRegisteredAsSpringBean() {
        // Assert
        assertNotNull(eventHandler, "UserRegisteredEventHandler sollte als Spring Bean registriert sein");
    }

    @Test
    void handleUserRegistered_shouldHandleMultipleEvents() {
        // Arrange
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        UserRegisteredEvent event1 = new UserRegisteredEvent(userId1, "user1@mail.de");
        UserRegisteredEvent event2 = new UserRegisteredEvent(userId2, "user2@mail.de");

        // Act & Assert - sollte keine Exception werfen
        assertDoesNotThrow(() -> {
            eventPublisher.publishEvent(event1);
            eventPublisher.publishEvent(event2);
            
            // Wait for processing
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Handler sollte mehrere Events ohne Fehler verarbeiten können");
    }
}

