package com.example.cityfeedback.feedbackmanagement.application;

import com.example.cityfeedback.feedbackmanagement.domain.model.Feedback;
import com.example.cityfeedback.feedbackmanagement.domain.model.Comment;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Category;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Status;
import com.example.cityfeedback.feedbackmanagement.infrastructure.FeedbackRepositoryImpl;
import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FeedbackControllerTest {

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private com.example.cityfeedback.usermanagement.domain.repositories.UserRepository userRepository;

    @Autowired
    private FeedbackRepositoryImpl feedbackRepository;

    @Autowired
    private FeedbackService feedbackService;

    private User testUser;
    private static int userCounter = 0;

    @BeforeEach
    void setUp() {
        // Eindeutige E-Mail-Adresse für jeden Test
        String uniqueEmail = "controller-test" + (userCounter++) + "@mail.de";
        testUser = new User(new Email(uniqueEmail), new Password("Abcdef12"), UserRole.CITIZEN);
        testUser = userRepository.save(testUser);
    }

    @Test
    void postFeedback_shouldReturn200() {
        // User vorbereiten
        User user = new User(new Email("controller@test.de"), new Password("Abcdef12"), UserRole.CITIZEN);
        user = userRepository.save(user);

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

    // ===================================================================
    // Collection Processing Controller Tests - Funktionale Programmierung
    // ===================================================================

    @Test
    void getFeedbackStatusStatistics_shouldReturn200AndMap() {
        // Arrange: Erstelle Feedbacks mit verschiedenen Status
        FeedbackDTO dto1 = createFeedbackDTO("Feedback 1", Category.VERKEHR);
        feedbackService.createFeedback(dto1);

        FeedbackDTO dto2 = createFeedbackDTO("Feedback 2", Category.UMWELT);
        Feedback feedback2 = feedbackService.createFeedback(dto2);
        feedback2.updateStatus(Status.INPROGRESS);
        feedbackRepository.save(feedback2);

        // Act: REST-Request
        ResponseEntity<Map> response = rest.getForEntity("/feedback/statistics/status", Map.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Map<String, Object> statistics = response.getBody();
        assertFalse(statistics.isEmpty(), "Statistik-Map sollte nicht leer sein");
    }

    @Test
    void getFeedbackTitlesByCategory_shouldReturn200AndMap() {
        // Arrange
        FeedbackDTO dto1 = createFeedbackDTO("Verkehrsproblem", Category.VERKEHR);
        feedbackService.createFeedback(dto1);

        FeedbackDTO dto2 = createFeedbackDTO("Umweltproblem", Category.UMWELT);
        feedbackService.createFeedback(dto2);

        // Act: REST-Request
        ResponseEntity<Map> response = rest.getForEntity("/feedback/statistics/category", Map.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Map<String, Object> titlesByCategory = response.getBody();
        assertFalse(titlesByCategory.isEmpty(), "Kategorie-Map sollte nicht leer sein");
    }

    @Test
    void getPublishedActiveFeedbacksSummary_shouldReturn200AndList() {
        // Arrange: Erstelle veröffentlichtes Feedback
        FeedbackDTO dto = createFeedbackDTO("Veröffentlichtes Feedback", Category.VERKEHR);
        Feedback feedback = feedbackService.createFeedback(dto);
        feedback.publish();
        feedbackRepository.save(feedback);

        // Act: REST-Request
        ResponseEntity<FeedbackService.FeedbackSummaryDTO[]> response = 
                rest.getForEntity("/feedback/summary/published-active", FeedbackService.FeedbackSummaryDTO[].class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // Prüfe dass unser veröffentlichtes Feedback enthalten ist
        boolean found = false;
        for (FeedbackService.FeedbackSummaryDTO summary : response.getBody()) {
            if ("Veröffentlichtes Feedback".equals(summary.getTitle())) {
                found = true;
                assertNotNull(summary.getId());
                assertNotNull(summary.getTitle());
                assertNotNull(summary.getCategory());
                assertNotNull(summary.getStatus());
                assertNotNull(summary.getFeedbackDate());
                break;
            }
        }
        assertTrue(found, "Sollte unser veröffentlichtes Feedback enthalten");
    }

    @Test
    void getPublishedActiveFeedbacksSummary_whenNoPublishedFeedbacks_shouldReturnEmptyList() {
        // Arrange: Erstelle nur nicht-veröffentlichtes Feedback
        FeedbackDTO dto = createFeedbackDTO("Nicht veröffentlichtes Feedback", Category.VERKEHR);
        feedbackService.createFeedback(dto);

        // Act: REST-Request
        ResponseEntity<FeedbackService.FeedbackSummaryDTO[]> response = 
                rest.getForEntity("/feedback/summary/published-active", FeedbackService.FeedbackSummaryDTO[].class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // Die Liste kann leer sein oder andere veröffentlichte Feedbacks enthalten
        // Wichtig ist nur, dass unser nicht-veröffentlichtes Feedback NICHT enthalten ist
        boolean found = false;
        for (FeedbackService.FeedbackSummaryDTO summary : response.getBody()) {
            if ("Nicht veröffentlichtes Feedback".equals(summary.getTitle())) {
                found = true;
                break;
            }
        }
        assertFalse(found, "Nicht-veröffentlichtes Feedback sollte nicht in der Liste sein");
    }

    @Test
    void getFeedbackStatistics_shouldReturn200AndStatistics() {
        // Arrange: Erstelle einige Feedbacks
        FeedbackDTO dto1 = createFeedbackDTO("Feedback 1", Category.VERKEHR);
        Feedback feedback1 = feedbackService.createFeedback(dto1);
        feedback1.publish();
        feedbackRepository.save(feedback1);

        FeedbackDTO dto2 = createFeedbackDTO("Feedback 2", Category.UMWELT);
        feedbackService.createFeedback(dto2);

        // Act: REST-Request
        ResponseEntity<FeedbackService.FeedbackStatisticsDTO> response = 
                rest.getForEntity("/feedback/statistics", FeedbackService.FeedbackStatisticsDTO.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        FeedbackService.FeedbackStatisticsDTO statistics = response.getBody();
        
        assertNotNull(statistics.getTotalCount());
        assertNotNull(statistics.getPublishedCount());
        assertNotNull(statistics.getClosedCount());
        assertNotNull(statistics.getOpenCount());
        assertTrue(statistics.getTotalCount() >= 2L, "Sollte mindestens 2 Feedbacks haben");
        assertTrue(statistics.getPublishedCount() >= 1L, "Sollte mindestens 1 veröffentlichtes Feedback haben");
    }

    @Test
    void addComment_shouldReturn200() {
        // Arrange
        User staffUser = new User(new Email("staff@test.de"), new Password("Abcdef12"), UserRole.STAFF);
        staffUser = userRepository.save(staffUser);

        FeedbackDTO dto = createFeedbackDTO("Test Feedback", Category.VERKEHR);
        Feedback feedback = feedbackService.createFeedback(dto);

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.authorId = staffUser.getId();
        commentDTO.content = "Test-Kommentar";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CommentDTO> request = new HttpEntity<>(commentDTO, headers);

        // Act
        ResponseEntity<Comment> response = rest.postForEntity(
                "/feedback/" + feedback.getId() + "/comments", request, Comment.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test-Kommentar", response.getBody().getContent());
    }

    @Test
    void getComments_shouldReturn200AndList() {
        // Arrange
        User staffUser = new User(new Email("staff@test.de"), new Password("Abcdef12"), UserRole.STAFF);
        staffUser = userRepository.save(staffUser);

        FeedbackDTO dto = createFeedbackDTO("Test Feedback", Category.VERKEHR);
        Feedback feedback = feedbackService.createFeedback(dto);

        // Füge Kommentare hinzu
        feedbackService.addComment(feedback.getId(), staffUser.getId(), "Kommentar 1");
        feedbackService.addComment(feedback.getId(), staffUser.getId(), "Kommentar 2");

        // Act
        ResponseEntity<Comment[]> response = rest.getForEntity(
                "/feedback/" + feedback.getId() + "/comments", Comment[].class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length >= 2);
    }

    @Test
    void deleteFeedback_asAdmin_shouldReturn204() {
        // Arrange
        User adminUser = new User(new Email("admin@test.de"), new Password("Abcdef12"), UserRole.ADMIN);
        adminUser = userRepository.save(adminUser);

        FeedbackDTO dto = createFeedbackDTO("Test Feedback", Category.VERKEHR);
        Feedback feedback = feedbackService.createFeedback(dto);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Admin-Id", adminUser.getId().toString());

        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<Void> response = rest.exchange(
                "/feedback/" + feedback.getId(),
                org.springframework.http.HttpMethod.DELETE,
                request,
                Void.class);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    // Hilfsmethode
    private FeedbackDTO createFeedbackDTO(String title, Category category) {
        FeedbackDTO dto = new FeedbackDTO();
        dto.userId = testUser.getId();
        dto.title = title;
        dto.category = category;
        dto.content = "Test Content";
        return dto;
    }
}
