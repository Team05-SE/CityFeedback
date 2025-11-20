package com.example.cityfeedback.feedbackmanagement.infrastructure;

import com.example.cityfeedback.feedbackmanagement.domain.model.Feedback;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Category;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Status;
import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FeedbackRepositoryTest {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private com.example.cityfeedback.usermanagement.infrastructure.UserRepository userRepository;

    @Test
    void saveAndLoadFeedback() {

        User user = new User(new Email("repo@mail.de"), new Password("Abcdef12"), UserRole.CITIZEN);
        userRepository.save(user);

        Feedback feedback = new Feedback(
                null,
                "Title",
                Category.UMWELT,
                LocalDate.now(),
                "Test content",
                Status.OPEN,
                false
        );
        feedback.setUser(user);

        Feedback saved = feedbackRepository.save(feedback);

        assertNotNull(saved.getId());
        assertEquals("Title", saved.getTitle());
        assertEquals(user.getId(), saved.getUser().getId());
    }

}
