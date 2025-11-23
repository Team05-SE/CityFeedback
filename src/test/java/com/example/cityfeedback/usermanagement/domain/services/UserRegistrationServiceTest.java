package com.example.cityfeedback.usermanagement.domain.services;

import com.example.cityfeedback.usermanagement.domain.exceptions.EmailAlreadyExistsException;
import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.repositories.UserRepository;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-Tests für UserRegistrationService.
 * Keine Spring-Abhängigkeiten - reine Domain-Logik-Tests.
 */
class UserRegistrationServiceTest {

    private UserRegistrationService userRegistrationService;
    private InMemoryUserRepository userRepository;
    private MockPasswordHasher passwordHasher;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        passwordHasher = new MockPasswordHasher();
        userRegistrationService = new UserRegistrationService(userRepository, passwordHasher);
    }

    @Test
    void registerUser_shouldCreateNewUser() {
        // Arrange
        Email email = new Email("test@mail.de");
        String rawPassword = "Abcdef12";

        // Act
        User user = userRegistrationService.registerUser(email, rawPassword);

        // Assert
        assertNotNull(user);
        assertEquals(email, user.getEmail());
        assertEquals(UUID.class, user.getId().getClass());
    }

    @Test
    void registerUser_shouldThrowWhenEmailAlreadyExists() {
        // Arrange
        Email email = new Email("existing@mail.de");
        String rawPassword = "Abcdef12";
        
        // Ersten User registrieren
        userRegistrationService.registerUser(email, rawPassword);

        // Act & Assert
        assertThrows(EmailAlreadyExistsException.class, () -> {
            userRegistrationService.registerUser(email, "AnotherPass12");
        });
    }

    @Test
    void registerUser_shouldValidatePassword() {
        // Arrange
        Email email = new Email("test@mail.de");
        String invalidPassword = "short"; // Zu kurz

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userRegistrationService.registerUser(email, invalidPassword);
        });
    }

    // In-Memory Repository für Unit-Tests
    private static class InMemoryUserRepository implements UserRepository {
        private final Map<UUID, User> users = new HashMap<>();
        private final Map<String, User> usersByEmail = new HashMap<>();

        @Override
        public void save(User user) {
            // Wenn User noch keine ID hat, generiere eine
            if (user.getId() == null) {
                user.setId(UUID.randomUUID());
            }
            users.put(user.getId(), user);
            usersByEmail.put(user.getEmail().getValue(), user);
        }

        @Override
        public Optional<User> findById(UUID id) {
            return Optional.ofNullable(users.get(id));
        }

        @Override
        public java.util.List<User> findAll() {
            return new java.util.ArrayList<>(users.values());
        }

        @Override
        public boolean existsByEmail(Email email) {
            return usersByEmail.containsKey(email.getValue());
        }

        @Override
        public Optional<User> findByEmail(Email email) {
            return Optional.ofNullable(usersByEmail.get(email.getValue()));
        }
    }

    // Mock PasswordHasher für Unit-Tests
    private static class MockPasswordHasher implements PasswordHasher {
        @Override
        public String hash(String rawPassword) {
            return "hashed_" + rawPassword;
        }

        @Override
        public boolean matches(String rawPassword, String hashedPassword) {
            return hashedPassword.equals("hashed_" + rawPassword);
        }
    }
}

