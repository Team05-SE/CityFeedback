package com.example.cityfeedback.usermanagement.domain.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain Event: UserRegisteredEvent
 * Wird ausgelöst, wenn sich ein neuer User erfolgreich registriert hat.
 */
public class UserRegisteredEvent {

    private final UUID userId;
    private final String email;
    private final Instant occurredOn;

    public UserRegisteredEvent(UUID userId, String email) {
        this.userId = userId;
        this.email = email;
        this.occurredOn = Instant.now();
    }

    public UUID getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public Instant getOccurredOn() {
        return occurredOn;
    }

    @Override
    public String toString() {
        return "UserRegisteredEvent{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", occurredOn=" + occurredOn +
                '}';
    }
}