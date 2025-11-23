package com.example.cityfeedback.feedbackmanagement.infrastructure;

import com.example.cityfeedback.feedbackmanagement.domain.model.Feedback;
import com.example.cityfeedback.feedbackmanagement.domain.repositories.FeedbackRepository;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Category;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Status;
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

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FeedbackRepositoryTest {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordHasher passwordHasher;

    @Test
    void saveAndLoadFeedback() {
        // Arrange: User erstellen - ohne ID (wird beim Speichern gesetzt)
        Email email = new Email("repo@mail.de");
        Password password = Password.create("Abcdef12", passwordHasher);
        User user = new User(email, password, UserRole.CITIZEN);
        // Keine ID setzen - wird beim Speichern generiert
        userRepository.save(user);

        // Arrange: Feedback erstellen
        UserId creatorId = new UserId(user.getId());
        Feedback feedback = new Feedback(
                null,
                "Title",
                Category.UMWELT,
                LocalDate.now(),
                "Test content",
                Status.OPEN,
                false,
                creatorId
        );

        // Act
        feedbackRepository.save(feedback);

        // Assert
        assertNotNull(feedback.getId());
        assertEquals("Title", feedback.getTitle());
        assertEquals(user.getId(), feedback.getCreatorId().getValue());
    }

}
