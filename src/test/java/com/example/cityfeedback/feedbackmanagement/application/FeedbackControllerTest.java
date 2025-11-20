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
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FeedbackControllerTest {

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private com.example.cityfeedback.usermanagement.infrastructure.UserRepository userRepository;

    @Test
    void postFeedback_shouldReturn200() {
        // User vorbereiten
        User user = new User(new Email("controller@test.de"), new Password("Abcdef12"), UserRole.CITIZEN);
        userRepository.save(user);

        FeedbackDTO dto = new FeedbackDTO();
        dto.userId = user.getId();
        dto.title = "Test";
        dto.category = Category.UMWELT;
        dto.content = "Controller test";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<FeedbackDTO> request = new HttpEntity<>(dto, headers);

        ResponseEntity<Feedback> response = rest.postForEntity("/feedback", request, Feedback.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());
    }
}
