package com.example.cityfeedback.feedbackmanagement.infrastructure;

import com.example.cityfeedback.feedbackmanagement.domain.model.Comment;
import com.example.cityfeedback.feedbackmanagement.domain.model.Feedback;
import com.example.cityfeedback.feedbackmanagement.domain.repositories.CommentRepository;
import com.example.cityfeedback.feedbackmanagement.domain.repositories.FeedbackRepository;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Category;
import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import com.example.cityfeedback.usermanagement.domain.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Feedback testFeedback;
    private static int userCounter = 0;

    @BeforeEach
    void setUp() {
        // Eindeutige E-Mail-Adresse für jeden Test
        String uniqueEmail = "comment-test" + (userCounter++) + "@mail.de";
        testUser = new User(new Email(uniqueEmail), new Password("Abcdef12"), UserRole.CITIZEN);
        testUser = userRepository.save(testUser);

        testFeedback = Feedback.create("Test Feedback", Category.VERKEHR, "Content", testUser.getId());
        testFeedback = feedbackRepository.save(testFeedback);
    }

    @Test
    void save_shouldPersistComment() {
        // Arrange
        Comment comment = new Comment(testFeedback.getId(), testUser.getId(), "Test-Kommentar");

        // Act
        Comment saved = commentRepository.save(comment);

        // Assert
        assertNotNull(saved.getId());
        assertEquals(testFeedback.getId(), saved.getFeedbackId());
        assertEquals(testUser.getId(), saved.getAuthorId());
        assertEquals("Test-Kommentar", saved.getContent());
        assertNotNull(saved.getCreatedAt());
    }

    @Test
    void findByFeedbackId_shouldReturnCommentsForFeedback() {
        // Arrange
        Comment comment1 = new Comment(testFeedback.getId(), testUser.getId(), "Erster Kommentar");
        commentRepository.save(comment1);

        Comment comment2 = new Comment(testFeedback.getId(), testUser.getId(), "Zweiter Kommentar");
        commentRepository.save(comment2);

        // Erstelle ein anderes Feedback mit Kommentar
        Feedback otherFeedback = Feedback.create("Anderes Feedback", Category.UMWELT, "Content", testUser.getId());
        otherFeedback = feedbackRepository.save(otherFeedback);
        Comment otherComment = new Comment(otherFeedback.getId(), testUser.getId(), "Anderer Kommentar");
        commentRepository.save(otherComment);

        // Act
        List<Comment> comments = commentRepository.findByFeedbackId(testFeedback.getId());

        // Assert
        assertNotNull(comments);
        assertEquals(2, comments.size());
        assertTrue(comments.stream().anyMatch(c -> c.getContent().equals("Erster Kommentar")));
        assertTrue(comments.stream().anyMatch(c -> c.getContent().equals("Zweiter Kommentar")));
        assertFalse(comments.stream().anyMatch(c -> c.getContent().equals("Anderer Kommentar")));
    }

    @Test
    void findByFeedbackId_shouldReturnCommentsInChronologicalOrder() {
        // Arrange
        Comment comment1 = new Comment(testFeedback.getId(), testUser.getId(), "Erster Kommentar");
        commentRepository.save(comment1);

        // Warte kurz, damit Zeitstempel unterschiedlich ist
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Comment comment2 = new Comment(testFeedback.getId(), testUser.getId(), "Zweiter Kommentar");
        commentRepository.save(comment2);

        // Act
        List<Comment> comments = commentRepository.findByFeedbackId(testFeedback.getId());

        // Assert
        assertEquals(2, comments.size());
        // Ältester Kommentar sollte zuerst sein
        assertEquals("Erster Kommentar", comments.get(0).getContent());
        assertEquals("Zweiter Kommentar", comments.get(1).getContent());
    }

    @Test
    void findByFeedbackId_withNoComments_shouldReturnEmptyList() {
        // Act
        List<Comment> comments = commentRepository.findByFeedbackId(testFeedback.getId());

        // Assert
        assertNotNull(comments);
        assertTrue(comments.isEmpty());
    }

    @Test
    void deleteByFeedbackId_shouldDeleteAllCommentsForFeedback() {
        // Arrange
        Comment comment1 = new Comment(testFeedback.getId(), testUser.getId(), "Kommentar 1");
        commentRepository.save(comment1);

        Comment comment2 = new Comment(testFeedback.getId(), testUser.getId(), "Kommentar 2");
        commentRepository.save(comment2);

        // Erstelle ein anderes Feedback mit Kommentar
        Feedback otherFeedback = Feedback.create("Anderes Feedback", Category.UMWELT, "Content", testUser.getId());
        otherFeedback = feedbackRepository.save(otherFeedback);
        Comment otherComment = new Comment(otherFeedback.getId(), testUser.getId(), "Anderer Kommentar");
        commentRepository.save(otherComment);

        // Act
        commentRepository.deleteByFeedbackId(testFeedback.getId());

        // Assert
        List<Comment> comments = commentRepository.findByFeedbackId(testFeedback.getId());
        assertTrue(comments.isEmpty());

        // Anderes Feedback sollte noch Kommentare haben
        List<Comment> otherComments = commentRepository.findByFeedbackId(otherFeedback.getId());
        assertEquals(1, otherComments.size());
    }
}

