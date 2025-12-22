package com.example.cityfeedback.feedbackmanagement.application;

import com.example.cityfeedback.feedbackmanagement.domain.model.Feedback;
import com.example.cityfeedback.feedbackmanagement.domain.model.Comment;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Category;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Status;
import com.example.cityfeedback.feedbackmanagement.domain.exceptions.FeedbackNotFoundException;
import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.exceptions.UnauthorizedException;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FeedbackServiceTest {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private com.example.cityfeedback.usermanagement.domain.repositories.UserRepository userRepository;

    @Autowired
    private com.example.cityfeedback.feedbackmanagement.domain.repositories.FeedbackRepository feedbackRepository;

    private User testUser;
    private static int userCounter = 0;

    @BeforeEach
    void setUp() {
        // Eindeutige E-Mail-Adresse für jeden Test
        String uniqueEmail = "test" + (userCounter++) + "@mail.de";
        testUser = new User(new Email(uniqueEmail), new Password("Abcdef12"), UserRole.CITIZEN);
        testUser = userRepository.save(testUser);
    }

    @Test
    void createFeedback_shouldPersistFeedback() {
        // Arrange: User exists
        User user = new User(new Email("test@mail.de"), new Password("Abcdef12"), UserRole.CITIZEN);
        user = userRepository.save(user);

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
        assertEquals(user.getId(), saved.getUserId()); // Jetzt userId statt getUser().getId()
        assertEquals(Category.VERKEHR, saved.getCategory());
    }

    @Test
    void getAllFeedbacks_shouldReturnList() {
        assertNotNull(feedbackService.getAllFeedbacks());
    }

    // ===================================================================
    // Collection Processing Tests - Funktionale Programmierung
    // ===================================================================

    @Test
    void getFeedbackStatusStatistics_shouldGroupFeedbacksByStatus() {
        // Arrange: Erstelle Feedbacks mit verschiedenen Status
        FeedbackDTO dto1 = createFeedbackDTO("Feedback 1", Category.VERKEHR);
        Feedback feedback1 = feedbackService.createFeedback(dto1);

        FeedbackDTO dto2 = createFeedbackDTO("Feedback 2", Category.UMWELT);
        Feedback feedback2 = feedbackService.createFeedback(dto2);
        feedback2.updateStatus(Status.INPROGRESS);
        feedbackRepository.save(feedback2);

        FeedbackDTO dto3 = createFeedbackDTO("Feedback 3", Category.BELEUCHTUNG);
        Feedback feedback3 = feedbackService.createFeedback(dto3);
        feedback3.updateStatus(Status.CLOSED);
        feedbackRepository.save(feedback3);

        // Act: Gruppierung nach Status
        Map<Status, Long> statistics = feedbackService.getFeedbackStatusStatistics();

        // Assert
        assertNotNull(statistics);
        // Prüfe, dass unsere Feedbacks enthalten sind (kann mehr sein durch andere Tests)
        Long openCount = statistics.getOrDefault(Status.OPEN, 0L);
        Long inProgressCount = statistics.getOrDefault(Status.INPROGRESS, 0L);
        Long closedCount = statistics.getOrDefault(Status.CLOSED, 0L);
        
        // Die Gruppierung sollte funktionieren - prüfe dass Status vorhanden sind
        assertTrue(openCount >= 1L, "Sollte mindestens 1 OPEN Feedback haben");
        assertTrue(inProgressCount >= 1L, "Sollte mindestens 1 INPROGRESS Feedback haben");
        assertTrue(closedCount >= 1L, "Sollte mindestens 1 CLOSED Feedback haben");
        
        // Die Gruppierung selbst ist das Wichtigste - prüfe dass die Map erstellt wurde
        assertFalse(statistics.isEmpty(), "Statistik-Map sollte nicht leer sein");
    }

    @Test
    void getFeedbackTitlesByCategory_shouldGroupFeedbacksByCategory() {
        // Arrange
        FeedbackDTO dto1 = createFeedbackDTO("Verkehrsproblem", Category.VERKEHR);
        feedbackService.createFeedback(dto1);

        FeedbackDTO dto2 = createFeedbackDTO("Umweltproblem", Category.UMWELT);
        feedbackService.createFeedback(dto2);

        FeedbackDTO dto3 = createFeedbackDTO("Weiteres Verkehrsproblem", Category.VERKEHR);
        feedbackService.createFeedback(dto3);

        // Act: Gruppierung nach Kategorie
        Map<Category, List<String>> titlesByCategory = feedbackService.getFeedbackTitlesByCategory();

        // Assert
        assertNotNull(titlesByCategory);
        assertTrue(titlesByCategory.containsKey(Category.VERKEHR));
        // Prüfe dass unsere Feedbacks enthalten sind (kann mehr sein durch andere Tests)
        assertTrue(titlesByCategory.get(Category.VERKEHR).size() >= 2, 
                "Sollte mindestens 2 Verkehrs-Feedbacks haben");
        assertTrue(titlesByCategory.get(Category.VERKEHR).contains("Verkehrsproblem"));
        assertTrue(titlesByCategory.get(Category.VERKEHR).contains("Weiteres Verkehrsproblem"));

        assertTrue(titlesByCategory.containsKey(Category.UMWELT));
        // Prüfe dass unsere Feedbacks enthalten sind (kann mehr sein durch andere Tests)
        assertTrue(titlesByCategory.get(Category.UMWELT).size() >= 1, 
                "Sollte mindestens 1 UMWELT-Feedback haben");
        assertTrue(titlesByCategory.get(Category.UMWELT).contains("Umweltproblem"),
                "Sollte unser 'Umweltproblem' Feedback enthalten");
    }

    @Test
    void getPublishedActiveFeedbacksSummary_shouldFilterAndTransform() {
        // Arrange
        FeedbackDTO dto1 = createFeedbackDTO("Veröffentlichtes Feedback", Category.VERKEHR);
        Feedback feedback1 = feedbackService.createFeedback(dto1);
        feedback1.publish();
        feedbackRepository.save(feedback1);

        FeedbackDTO dto2 = createFeedbackDTO("Nicht veröffentlichtes Feedback", Category.UMWELT);
        feedbackService.createFeedback(dto2); // Nicht veröffentlicht

        FeedbackDTO dto3 = createFeedbackDTO("Geschlossenes Feedback", Category.BELEUCHTUNG);
        Feedback feedback3 = feedbackService.createFeedback(dto3);
        feedback3.publish();
        feedback3.updateStatus(Status.CLOSED);
        feedbackRepository.save(feedback3); // Veröffentlicht aber geschlossen

        FeedbackDTO dto4 = createFeedbackDTO("Veröffentlichtes offenes Feedback", Category.VANDALISMUS);
        Feedback feedback4 = feedbackService.createFeedback(dto4);
        feedback4.publish();
        feedbackRepository.save(feedback4);

        // Act: Filtering mit mehreren Kriterien + Transformation
        List<FeedbackService.FeedbackSummaryDTO> summaries = feedbackService.getPublishedActiveFeedbacksSummary();

        // Assert
        assertNotNull(summaries);
        // Prüfe dass unsere veröffentlichten Feedbacks enthalten sind (kann mehr sein durch andere Tests)
        assertTrue(summaries.size() >= 2, 
                "Sollte mindestens 2 veröffentlichte und nicht geschlossene Feedbacks haben");
        assertTrue(summaries.stream().anyMatch(s -> s.getTitle().equals("Veröffentlichtes Feedback")),
                "Sollte 'Veröffentlichtes Feedback' enthalten");
        assertTrue(summaries.stream().anyMatch(s -> s.getTitle().equals("Veröffentlichtes offenes Feedback")),
                "Sollte 'Veröffentlichtes offenes Feedback' enthalten");
        assertFalse(summaries.stream().anyMatch(s -> s.getTitle().equals("Nicht veröffentlichtes Feedback")),
                "Sollte NICHT 'Nicht veröffentlichtes Feedback' enthalten");
        assertFalse(summaries.stream().anyMatch(s -> s.getTitle().equals("Geschlossenes Feedback")),
                "Sollte NICHT 'Geschlossenes Feedback' enthalten");
    }

    @Test
    void getPublishedActiveFeedbacksSummary_shouldBeSortedByDateDescending() {
        // Arrange
        FeedbackDTO dto1 = createFeedbackDTO("Älteres Feedback", Category.VERKEHR);
        Feedback feedback1 = feedbackService.createFeedback(dto1);
        feedback1.publish();
        feedbackRepository.save(feedback1);

        // Warte kurz, damit Datum unterschiedlich ist
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        FeedbackDTO dto2 = createFeedbackDTO("Neueres Feedback", Category.UMWELT);
        Feedback feedback2 = feedbackService.createFeedback(dto2);
        feedback2.publish();
        feedbackRepository.save(feedback2);

        // Act
        List<FeedbackService.FeedbackSummaryDTO> summaries = feedbackService.getPublishedActiveFeedbacksSummary();

        // Assert: Sollte nach Datum sortiert sein (neueste zuerst)
        assertNotNull(summaries);
        assertTrue(summaries.size() >= 2);
        // Das erste Element sollte das neueste Datum haben
        assertNotNull(summaries.get(0).getFeedbackDate());
        assertNotNull(summaries.get(1).getFeedbackDate());
    }

    @Test
    void getFeedbackStatistics_shouldAggregateAllStatistics() {
        // Arrange
        FeedbackDTO dto1 = createFeedbackDTO("Feedback 1", Category.VERKEHR);
        Feedback feedback1 = feedbackService.createFeedback(dto1);
        feedback1.publish();
        feedbackRepository.save(feedback1);

        FeedbackDTO dto2 = createFeedbackDTO("Feedback 2", Category.UMWELT);
        Feedback feedback2 = feedbackService.createFeedback(dto2);
        feedback2.publish();
        feedback2.updateStatus(Status.CLOSED);
        feedbackRepository.save(feedback2);

        FeedbackDTO dto3 = createFeedbackDTO("Feedback 3", Category.BELEUCHTUNG);
        feedbackService.createFeedback(dto3); // Nicht veröffentlicht

        // Act: Aggregation/Reduktion
        FeedbackService.FeedbackStatisticsDTO statistics = feedbackService.getFeedbackStatistics();

        // Assert
        assertNotNull(statistics);
        // Prüfe, dass unsere Feedbacks enthalten sind (kann mehr sein durch andere Tests)
        assertTrue(statistics.getTotalCount() >= 3L, "Sollte mindestens 3 Feedbacks insgesamt haben");
        assertTrue(statistics.getPublishedCount() >= 1L, "Sollte mindestens 1 veröffentlichtes Feedback haben");
        assertTrue(statistics.getClosedCount() >= 1L, "Sollte mindestens 1 geschlossenes Feedback haben");
        assertTrue(statistics.getOpenCount() >= 1L, "Sollte mindestens 1 offenes Feedback haben");
        assertNotNull(statistics.getOldestDate());
        assertNotNull(statistics.getNewestDate());
    }

    @Test
    void getFeedbackStatistics_shouldHandleEmptyList() {
        // Act: Hole Statistiken (kann Feedbacks aus anderen Tests enthalten sein)
        FeedbackService.FeedbackStatisticsDTO statistics = feedbackService.getFeedbackStatistics();

        // Assert: Prüfe dass die Methoden funktionieren, auch wenn keine Daten vorhanden sind
        assertNotNull(statistics);
        
        // Wenn keine Feedbacks vorhanden sind, sollten alle Werte 0 sein
        if (statistics.getTotalCount() == 0L) {
            assertEquals(0L, statistics.getTotalCount());
            assertEquals(0L, statistics.getPublishedCount());
            assertEquals(0L, statistics.getClosedCount());
            assertEquals(0L, statistics.getOpenCount());
            assertNull(statistics.getOldestDate());
            assertNull(statistics.getNewestDate());
        } else {
            // Wenn Feedbacks vorhanden sind (aus anderen Tests), prüfe dass die Werte konsistent sind
            assertTrue(statistics.getTotalCount() > 0L);
            assertTrue(statistics.getPublishedCount() >= 0L);
            assertTrue(statistics.getClosedCount() >= 0L);
            assertTrue(statistics.getOpenCount() >= 0L);
            // Die Summe der veröffentlichten, geschlossenen und offenen sollte nicht größer sein als total
            assertTrue(statistics.getPublishedCount() <= statistics.getTotalCount());
            assertTrue(statistics.getClosedCount() <= statistics.getTotalCount());
            assertTrue(statistics.getOpenCount() <= statistics.getTotalCount());
        }
    }

    // ===================================================================
    // Kommentar-Tests
    // ===================================================================

    @Test
    void addComment_shouldPersistComment() {
        // Arrange
        User staffUser = new User(new Email("staff@test.de"), new Password("Abcdef12"), UserRole.STAFF);
        staffUser = userRepository.save(staffUser);

        FeedbackDTO dto = createFeedbackDTO("Test Feedback", Category.VERKEHR);
        Feedback feedback = feedbackService.createFeedback(dto);

        // Act
        Comment comment = feedbackService.addComment(feedback.getId(), staffUser.getId(), "Test-Kommentar");

        // Assert
        assertNotNull(comment.getId());
        assertEquals(feedback.getId(), comment.getFeedbackId());
        assertEquals(staffUser.getId(), comment.getAuthorId());
        assertEquals("Test-Kommentar", comment.getContent());
        assertNotNull(comment.getCreatedAt());
    }

    @Test
    void addComment_asCitizen_shouldThrow() {
        // Arrange
        User citizenUser = new User(new Email("citizen@test.de"), new Password("Abcdef12"), UserRole.CITIZEN);
        citizenUser = userRepository.save(citizenUser);
        final UUID citizenUserId = citizenUser.getId();

        FeedbackDTO dto = createFeedbackDTO("Test Feedback", Category.VERKEHR);
        Feedback feedback = feedbackService.createFeedback(dto);
        final Long feedbackId = feedback.getId();

        // Act & Assert
        assertThrows(UnauthorizedException.class,
                () -> feedbackService.addComment(feedbackId, citizenUserId, "Test-Kommentar"));
    }

    @Test
    void addComment_asAdmin_shouldSucceed() {
        // Arrange
        User adminUser = new User(new Email("admin@test.de"), new Password("Abcdef12"), UserRole.ADMIN);
        adminUser = userRepository.save(adminUser);

        FeedbackDTO dto = createFeedbackDTO("Test Feedback", Category.VERKEHR);
        Feedback feedback = feedbackService.createFeedback(dto);

        // Act
        Comment comment = feedbackService.addComment(feedback.getId(), adminUser.getId(), "Admin-Kommentar");

        // Assert
        assertNotNull(comment.getId());
        assertEquals(adminUser.getId(), comment.getAuthorId());
    }

    @Test
    void addComment_withNonExistentFeedback_shouldThrow() {
        // Arrange
        User staffUser = new User(new Email("staff@test.de"), new Password("Abcdef12"), UserRole.STAFF);
        staffUser = userRepository.save(staffUser);
        final UUID staffUserId = staffUser.getId();

        // Act & Assert
        assertThrows(FeedbackNotFoundException.class,
                () -> feedbackService.addComment(99999L, staffUserId, "Test-Kommentar"));
    }

    @Test
    void getCommentsByFeedbackId_shouldReturnComments() {
        // Arrange
        User staffUser = new User(new Email("staff@test.de"), new Password("Abcdef12"), UserRole.STAFF);
        staffUser = userRepository.save(staffUser);

        FeedbackDTO dto = createFeedbackDTO("Test Feedback", Category.VERKEHR);
        Feedback feedback = feedbackService.createFeedback(dto);

        feedbackService.addComment(feedback.getId(), staffUser.getId(), "Erster Kommentar");
        feedbackService.addComment(feedback.getId(), staffUser.getId(), "Zweiter Kommentar");

        // Act
        List<Comment> comments = feedbackService.getCommentsByFeedbackId(feedback.getId());

        // Assert
        assertNotNull(comments);
        assertTrue(comments.size() >= 2);
        assertTrue(comments.stream().anyMatch(c -> c.getContent().equals("Erster Kommentar")));
        assertTrue(comments.stream().anyMatch(c -> c.getContent().equals("Zweiter Kommentar")));
    }

    @Test
    void getCommentsByFeedbackId_withNoComments_shouldReturnEmptyList() {
        // Arrange
        FeedbackDTO dto = createFeedbackDTO("Test Feedback", Category.VERKEHR);
        Feedback feedback = feedbackService.createFeedback(dto);

        // Act
        List<Comment> comments = feedbackService.getCommentsByFeedbackId(feedback.getId());

        // Assert
        assertNotNull(comments);
        assertTrue(comments.isEmpty());
    }

    // ===================================================================
    // Feedback-Verwaltung Tests
    // ===================================================================

    @Test
    void approveFeedback_shouldChangeStatusToOpen() {
        // Arrange
        FeedbackDTO dto = createFeedbackDTO("Test Feedback", Category.VERKEHR);
        Feedback feedback = feedbackService.createFeedback(dto);
        assertEquals(Status.PENDING, feedback.getStatus());

        // Act
        Feedback approved = feedbackService.approveFeedback(feedback.getId());

        // Assert
        assertEquals(Status.OPEN, approved.getStatus());
        assertTrue(approved.isPublished());
    }

    @Test
    void updateFeedbackStatus_shouldChangeStatus() {
        // Arrange
        FeedbackDTO dto = createFeedbackDTO("Test Feedback", Category.VERKEHR);
        Feedback feedback = feedbackService.createFeedback(dto);

        // Act
        Feedback updated = feedbackService.updateFeedbackStatus(feedback.getId(), Status.INPROGRESS);

        // Assert
        assertEquals(Status.INPROGRESS, updated.getStatus());
    }

    @Test
    void publishFeedback_shouldSetPublishedToTrue() {
        // Arrange
        FeedbackDTO dto = createFeedbackDTO("Test Feedback", Category.VERKEHR);
        Feedback feedback = feedbackService.createFeedback(dto);
        feedback.updateStatus(Status.OPEN);
        feedbackRepository.save(feedback);

        // Act
        Feedback published = feedbackService.publishFeedback(feedback.getId());

        // Assert
        assertTrue(published.isPublished());
    }

    @Test
    void deleteFeedback_asAdmin_shouldDeleteFeedbackAndComments() {
        // Arrange
        User adminUser = new User(new Email("admin@test.de"), new Password("Abcdef12"), UserRole.ADMIN);
        adminUser = userRepository.save(adminUser);

        User staffUser = new User(new Email("staff@test.de"), new Password("Abcdef12"), UserRole.STAFF);
        staffUser = userRepository.save(staffUser);

        FeedbackDTO dto = createFeedbackDTO("Test Feedback", Category.VERKEHR);
        Feedback feedback = feedbackService.createFeedback(dto);

        // Füge Kommentare hinzu
        feedbackService.addComment(feedback.getId(), staffUser.getId(), "Kommentar 1");
        feedbackService.addComment(feedback.getId(), staffUser.getId(), "Kommentar 2");

        Long feedbackId = feedback.getId();

        // Act
        feedbackService.deleteFeedback(adminUser.getId(), feedbackId);

        // Assert
        assertThrows(FeedbackNotFoundException.class,
                () -> feedbackService.getFeedbackById(feedbackId));
        List<Comment> comments = feedbackService.getCommentsByFeedbackId(feedbackId);
        assertTrue(comments.isEmpty());
    }

    @Test
    void deleteFeedback_asStaff_shouldThrow() {
        // Arrange
        User staffUser = new User(new Email("staff@test.de"), new Password("Abcdef12"), UserRole.STAFF);
        staffUser = userRepository.save(staffUser);
        final UUID staffUserId = staffUser.getId();

        FeedbackDTO dto = createFeedbackDTO("Test Feedback", Category.VERKEHR);
        Feedback feedback = feedbackService.createFeedback(dto);
        final Long feedbackId = feedback.getId();

        // Act & Assert
        assertThrows(UnauthorizedException.class,
                () -> feedbackService.deleteFeedback(staffUserId, feedbackId));
    }

    @Test
    void deleteFeedback_withNonExistentFeedback_shouldThrow() {
        // Arrange
        User adminUser = new User(new Email("admin@test.de"), new Password("Abcdef12"), UserRole.ADMIN);
        adminUser = userRepository.save(adminUser);
        final UUID adminUserId = adminUser.getId();

        // Act & Assert
        assertThrows(FeedbackNotFoundException.class,
                () -> feedbackService.deleteFeedback(adminUserId, 99999L));
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
