package com.example.cityfeedback.feedbackmanagement.application;

import com.example.cityfeedback.feedbackmanagement.domain.model.Feedback;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Category;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.UserId;
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
 * Integration-Tests für FeedbackService.
 * Testet die gesamte Anwendungsschicht mit Spring-Kontext.
 */
@SpringBootTest
@Transactional
class FeedbackServiceTest {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordHasher passwordHasher;

    @Test
    void createFeedback_shouldPersistFeedback() {
        // Arrange: User exists - erstelle ohne ID (wird beim Speichern gesetzt)
        Email email = new Email("test@mail.de");
        Password password = Password.create("Abcdef12", passwordHasher);
        User user = new User(email, password, UserRole.CITIZEN);
        // Keine ID setzen - wird beim Speichern generiert
        userRepository.save(user);

        FeedbackDTO dto = new FeedbackDTO();
        dto.userId = user.getId();
        dto.title = "Test-Feedback";
        dto.category = Category.VERKEHR;
        dto.content = "Lorem ipsum";

        // Act
        Feedback saved = feedbackService.createFeedback(dto);

        // Assert
        assertNotNull(saved.getId());
        assertEquals(dto.title, saved.getTitle());
        assertEquals(dto.content, saved.getContent());
        assertEquals(user.getId(), saved.getCreatorId().getValue());
        assertEquals(Category.VERKEHR, saved.getCategory());
    }

    @Test
    void getAllFeedbacks_shouldReturnList() {
        // Act
        var feedbacks = feedbackService.getAllFeedbacks();

        // Assert
        assertNotNull(feedbacks);
    }

    @Test
    void getFeedbackById_shouldReturnFeedback() {
        // Arrange: User und Feedback erstellen - User ohne ID
        Email email = new Email("test2@mail.de");
        Password password = Password.create("Abcdef12", passwordHasher);
        User user = new User(email, password, UserRole.CITIZEN);
        // Keine ID setzen - wird beim Speichern generiert
        userRepository.save(user);

        FeedbackDTO dto = new FeedbackDTO();
        dto.userId = user.getId();
        dto.title = "Test-Feedback-2";
        dto.category = Category.UMWELT;
        dto.content = "Test content";
        Feedback created = feedbackService.createFeedback(dto);

        // Act
        Feedback found = feedbackService.getFeedbackById(created.getId());

        // Assert
        assertNotNull(found);
        assertEquals(created.getId(), found.getId());
        assertEquals(dto.title, found.getTitle());
    }
}
