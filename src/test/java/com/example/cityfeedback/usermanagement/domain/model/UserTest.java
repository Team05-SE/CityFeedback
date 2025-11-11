package com.example.cityfeedback.usermanagement.domain.model;

import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void register_shouldAssignCitizenRole() {
        User user = User.register(new Email("test@mail.de"), new Password("Abcdef12"));
        Assertions.assertEquals(UserRole.CITIZEN, user.getRole());
    }

    @Test
    void register_shouldAssignUniqueId() {
        User user1 = User.register(new Email("a@mail.de"), new Password("Abcdef12"));
        User user2 = User.register(new Email("b@mail.de"), new Password("Abcdef12"));

        assertNotEquals(user1.getId(), user2.getId());
    }

    @Test
    void register_shouldStoreEmailCorrectly() {
        Email email = new Email("someone@mail.de");
        User user = User.register(email, new Password("Abcdef12"));

        assertEquals(email, user.getEmail());
    }
}