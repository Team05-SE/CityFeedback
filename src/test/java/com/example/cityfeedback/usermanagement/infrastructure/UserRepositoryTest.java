package com.example.cityfeedback.usermanagement.infrastructure;

import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveAndLoadUser() {
        User user = new User(
                new Email("repoTest@mail.de"),
                new Password("Abcdef12"),
                UserRole.CITIZEN
        );

        User saved = userRepository.save(user);

        assertNotNull(saved.getId());
        assertEquals("repotest@mail.de", saved.getEmail().getValue());
    }

    @Test
    void existsByEmail_shouldReturnTrue() {
        Email email = new Email("exists@mail.de");
        User user = new User(email, new Password("Abcdef12"), UserRole.CITIZEN);
        userRepository.save(user);

        assertTrue(userRepository.existsByEmail(email));
    }

    @Test
    void existsByEmail_shouldReturnFalse() {
        assertFalse(userRepository.existsByEmail(new Email("notfound@mail.de")));
    }
}
