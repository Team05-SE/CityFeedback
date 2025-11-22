package com.example.cityfeedback.feedbackmanagement.domain.model;

import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Category;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Status;
import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {

    @Test
    void constructor_shouldSetFieldsCorrectly() {
        Feedback feedback = new Feedback(
                1L,
                "Test",
                Category.UMWELT,
                LocalDate.of(2024, 1, 1),
                "Content",
                Status.OPEN,
                false
        );

        assertEquals("Test", feedback.getTitle());
        assertEquals("Content", feedback.getContent());
        assertEquals(Category.UMWELT, feedback.getCategory());
        assertEquals(Status.OPEN, feedback.getStatus());
    }

    @Test
    void setUser_shouldAssignUser() {
        User user = new User(
                new Email("entity@mail.de"),
                new Password("Abcdef12"),
                UserRole.CITIZEN
        );

        Feedback feedback = new Feedback();
        feedback.setUser(user);

        assertEquals(user, feedback.getUser());
    }

    @Test
    void setPublished_shouldWork() {
        Feedback feedback = new Feedback();
        feedback.setPublished(true);

        assertTrue(feedback.isPublished());
    }
}
