package com.example.cityfeedback.usermanagement.application;

import com.example.cityfeedback.usermanagement.domain.exceptions.UserNotFoundException;
import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import com.example.cityfeedback.usermanagement.domain.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void createUser_shouldPersistUser() {
        Email email = new Email("example@mail.com");
        Password pw = new Password("Abcdef12");

        User user = userService.createUser(email, pw, UserRole.CITIZEN);

        assertNotNull(user.getId());
        assertEquals(email, user.getEmail());
    }

    @Test
    void getUserById_shouldReturnUser() {
        User user = userRepository.save(new User(new Email("aa@mail.de"), new Password("Abcdef12"), UserRole.ADMIN));

        User found = userService.getUserById(user.getId());

        assertEquals(user.getId(), found.getId());
    }

    @Test
    void getUserById_notExisting_shouldThrow() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(UUID.randomUUID()));
    }
}
