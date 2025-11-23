package com.example.cityfeedback.usermanagement.domain.model;

import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import com.example.cityfeedback.usermanagement.domain.events.UserRegisteredEvent;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-Tests für User Aggregate Root.
 * Keine Spring-Abhängigkeiten - reine Domain-Logik-Tests.
 */
class UserTest {

    @Test
    void register_shouldAssignCitizenRole() {
        // Arrange
        Email email = new Email("test@mail.de");
        Password password = createPassword("Abcdef12");

        // Act
        User user = User.register(email, password);

        // Assert
        assertEquals(UserRole.CITIZEN, user.getRole());
    }

    @Test
    void register_shouldNotSetIdBeforeSaving() {
        // Arrange
        Email email = new Email("test@test.com");
        Password password = createPassword("Passwort123");

        // Act
        User user = User.register(email, password);

        // Assert
        // ID wird erst beim Speichern im Repository gesetzt
        // In der Domain-Logik ist die ID null, bis sie persistiert wird
        assertNull(user.getId());
    }

    @Test
    void register_shouldStoreEmailCorrectly() {
        // Arrange
        Email email = new Email("someone@mail.de");
        Password password = createPassword("Abcdef12");

        // Act
        User user = User.register(email, password);

        // Assert
        assertEquals(email, user.getEmail());
    }

    @Test
    void register_shouldCreateDomainEvent() {
        // Arrange
        Email email = new Email("test@mail.de");
        Password password = createPassword("Abcdef12");

        // Act
        User user = User.register(email, password);

        // Assert
        assertFalse(user.getDomainEvents().isEmpty());
        assertTrue(user.getDomainEvents().get(0) instanceof UserRegisteredEvent);
    }

    @Test
    void constructor_shouldRequireNonNullEmail() {
        // Arrange & Act & Assert
        assertThrows(NullPointerException.class, () -> {
            new User(null, createPassword("Abcdef12"), UserRole.CITIZEN);
        });
    }

    @Test
    void constructor_shouldRequireNonNullPassword() {
        // Arrange & Act & Assert
        assertThrows(NullPointerException.class, () -> {
            new User(new Email("test@mail.de"), null, UserRole.CITIZEN);
        });
    }

    @Test
    void constructor_shouldRequireNonNullRole() {
        // Arrange & Act & Assert
        assertThrows(NullPointerException.class, () -> {
            new User(new Email("test@mail.de"), createPassword("Abcdef12"), null);
        });
    }

    @Test
    void clearDomainEvents_shouldRemoveAllEvents() {
        // Arrange
        Email email = new Email("test@mail.de");
        Password password = createPassword("Abcdef12");
        User user = User.register(email, password);

        // Act
        user.clearDomainEvents();

        // Assert
        assertTrue(user.getDomainEvents().isEmpty());
    }

    // Helper-Methode für Password-Erstellung (ohne Spring)
    private Password createPassword(String rawPassword) {
        // Mock PasswordHasher für Unit-Tests
        com.example.cityfeedback.usermanagement.domain.services.PasswordHasher mockHasher = 
            new com.example.cityfeedback.usermanagement.domain.services.PasswordHasher() {
                @Override
                public String hash(String rawPassword) {
                    return "hashed_" + rawPassword; // Einfacher Mock-Hash
                }

                @Override
                public boolean matches(String rawPassword, String hashedPassword) {
                    return hashedPassword.equals("hashed_" + rawPassword);
                }
            };
        
        return Password.create(rawPassword, mockHasher);
    }
}
