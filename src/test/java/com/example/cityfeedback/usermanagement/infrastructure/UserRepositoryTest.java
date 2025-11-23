package com.example.cityfeedback.usermanagement.infrastructure;

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

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordHasher passwordHasher;

    @Test
    void saveAndLoadUser() {
        // Arrange - User ohne ID (wird beim Speichern gesetzt)
        Email email = new Email("repoTest@mail.de");
        Password password = Password.create("Abcdef12", passwordHasher);
        User user = new User(email, password, UserRole.CITIZEN);
        // Keine ID setzen - wird beim Speichern generiert

        // Act
        userRepository.save(user);

        // Assert
        assertNotNull(user.getId());
        assertEquals("repotest@mail.de", user.getEmail().getValue());
    }

    @Test
    void existsByEmail_shouldReturnTrue() {
        // Arrange - User ohne ID (wird beim Speichern gesetzt)
        Email email = new Email("exists@mail.de");
        Password password = Password.create("Abcdef12", passwordHasher);
        User user = new User(email, password, UserRole.CITIZEN);
        // Keine ID setzen - wird beim Speichern generiert
        userRepository.save(user);

        // Act & Assert
        assertTrue(userRepository.existsByEmail(email));
    }

    @Test
    void existsByEmail_shouldReturnFalse() {
        // Act & Assert
        assertFalse(userRepository.existsByEmail(new Email("notfound@mail.de")));
    }
}
