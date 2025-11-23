package com.example.cityfeedback.usermanagement.application;

import com.example.cityfeedback.usermanagement.domain.exceptions.InvalidPasswordException;
import com.example.cityfeedback.usermanagement.domain.exceptions.UserNotFoundException;
import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.repositories.UserRepository;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import com.example.cityfeedback.usermanagement.domain.services.PasswordHasher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration-Tests für UserService.
 * Testet die gesamte Anwendungsschicht mit Spring-Kontext.
 */
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordHasher passwordHasher;

    @Test
    void createUser_shouldPersistUser() {
        // Arrange
        String email = "example@mail.com";
        String password = "Abcdef12";

        // Act
        User user = userService.createUser(email, password, UserRole.CITIZEN);

        // Assert
        assertNotNull(user.getId());
        assertEquals(new Email(email), user.getEmail());
    }

    @Test
    void getUserById_shouldReturnUser() {
        // Arrange - verwende UserService, um User korrekt zu erstellen
        String email = "aa@mail.de";
        String password = "Abcdef12";
        User user = userService.createUser(email, password, UserRole.CITIZEN);

        // Act
        User found = userService.getUserById(user.getId());

        // Assert
        assertEquals(user.getId(), found.getId());
    }

    @Test
    void getUserById_notExisting_shouldThrow() {
        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> 
            userService.getUserById(UUID.randomUUID())
        );
    }

    @Test
    void login_shouldReturnUserWithValidCredentials() {
        // Arrange
        String email = "login@mail.de";
        String password = "Abcdef12";
        userService.createUser(email, password, UserRole.CITIZEN);

        // Act
        User loggedInUser = userService.login(email, password);

        // Assert
        assertNotNull(loggedInUser);
        assertEquals(new Email(email), loggedInUser.getEmail());
    }

    @Test
    void login_shouldThrowWithInvalidPassword() {
        // Arrange
        String email = "login2@mail.de";
        String password = "Abcdef12";
        userService.createUser(email, password, UserRole.CITIZEN);

        // Act & Assert
        assertThrows(InvalidPasswordException.class, () -> 
            userService.login(email, "WrongPassword12")
        );
    }
}
