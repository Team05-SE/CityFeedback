package com.example.cityfeedback.feedbackmanagement.domain.model;

import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Category;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Status;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.UserId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-Tests für Feedback Aggregate Root.
 * Keine Spring-Abhängigkeiten - reine Domain-Logik-Tests.
 */
class FeedbackTest {

    @Test
    void constructor_shouldSetFieldsCorrectly() {
        // Arrange
        Long id = 1L;
        String title = "Test";
        Category category = Category.UMWELT;
        LocalDate feedbackDate = LocalDate.of(2024, 1, 1);
        String content = "Content";
        Status status = Status.OPEN;
        boolean isPublished = false;
        UserId creatorId = new UserId(UUID.randomUUID());

        // Act
        Feedback feedback = new Feedback(id, title, category, feedbackDate, content, status, isPublished, creatorId);

        // Assert
        assertEquals(title, feedback.getTitle());
        assertEquals(content, feedback.getContent());
        assertEquals(category, feedback.getCategory());
        assertEquals(status, feedback.getStatus());
        assertEquals(creatorId, feedback.getCreatorId());
    }

    @Test
    void constructor_shouldRequireNonNullTitle() {
        // Arrange & Act & Assert
        assertThrows(NullPointerException.class, () -> {
            new Feedback(1L, null, Category.UMWELT, LocalDate.now(), "Content", Status.OPEN, false, new UserId(UUID.randomUUID()));
        });
    }

    @Test
    void constructor_shouldRequireNonNullCreatorId() {
        // Arrange & Act & Assert
        assertThrows(NullPointerException.class, () -> {
            new Feedback(1L, "Title", Category.UMWELT, LocalDate.now(), "Content", Status.OPEN, false, null);
        });
    }

    @Test
    void setPublished_shouldWork() {
        // Arrange
        Feedback feedback = new Feedback();
        
        // Act
        feedback.setPublished(true);

        // Assert
        assertTrue(feedback.isPublished());
    }

    @Test
    void setCreatorId_shouldRequireNonNull() {
        // Arrange
        Feedback feedback = new Feedback();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            feedback.setCreatorId(null);
        });
    }
}
