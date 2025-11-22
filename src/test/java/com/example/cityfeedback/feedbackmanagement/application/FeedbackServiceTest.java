package com.example.cityfeedback.feedbackmanagement.application;

import com.example.cityfeedback.feedbackmanagement.domain.model.Feedback;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Category;
import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FeedbackServiceTest {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private com.example.cityfeedback.usermanagement.infrastructure.UserRepository userRepository;

    @Autowired
    private com.example.cityfeedback.feedbackmanagement.infrastructure.FeedbackRepository feedbackRepository;

    @Test
    void createFeedback_shouldPersistFeedback() {
        // Arrange: User exists
        User user = new User(new Email("test@mail.de"), new Password("Abcdef12"), UserRole.CITIZEN);
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
        assertEquals(user.getId(), saved.getUser().getId());
        assertEquals(Category.VERKEHR, saved.getCategory());
    }

    @Test
    void getAllFeedbacks_shouldReturnList() {
        assertNotNull(feedbackService.getAllFeedbacks());
    }
}
