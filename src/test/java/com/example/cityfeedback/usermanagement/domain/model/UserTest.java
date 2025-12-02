package com.example.cityfeedback.usermanagement.domain.model;

import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import com.example.cityfeedback.usermanagement.domain.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void register_shouldAssignCitizenRole() {
        User user = User.register(new Email("test@mail.de"), new Password("Abcdef12"));
        assertEquals(UserRole.CITIZEN, user.getRole());
    }

    @Test
    void register_shouldAssignUniqueId() {
        User user = User.register(new Email("test@test.com"), new Password("Passwort123"));
        User saved = userRepository.save(user);

        assertNotNull(saved.getId());
    }

    @Test
    void register_shouldStoreEmailCorrectly() {
        Email email = new Email("someone@mail.de");
        User user = User.register(email, new Password("Abcdef12"));

        assertEquals(email, user.getEmail());
    }
}