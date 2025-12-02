package com.example.cityfeedback.feedbackmanagement.infrastructure;

import com.example.cityfeedback.feedbackmanagement.domain.model.Feedback;
import com.example.cityfeedback.feedbackmanagement.domain.repositories.FeedbackRepository;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Category;
import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.repositories.UserRepository;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FeedbackRepositoryTest {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveAndLoadFeedback() {
        User user = new User(new Email("repo@mail.de"), new Password("Abcdef12"), UserRole.CITIZEN);
        user = userRepository.save(user);

        Feedback feedback = Feedback.create(
                "Title",
                Category.UMWELT,
                "Test content",
                user.getId()
        );

        Feedback saved = feedbackRepository.save(feedback);

        assertNotNull(saved.getId());
        assertEquals("Title", saved.getTitle());
        assertEquals(user.getId(), saved.getUserId());
    }

}
