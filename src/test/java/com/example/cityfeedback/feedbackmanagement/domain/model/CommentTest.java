package com.example.cityfeedback.feedbackmanagement.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    @Test
    void constructor_shouldSetFieldsCorrectly() {
        // Arrange
        Long feedbackId = 1L;
        UUID authorId = UUID.randomUUID();
        String content = "Test-Kommentar";

        // Act
        Comment comment = new Comment(feedbackId, authorId, content);

        // Assert
        assertEquals(feedbackId, comment.getFeedbackId());
        assertEquals(authorId, comment.getAuthorId());
        assertEquals(content, comment.getContent());
        assertNotNull(comment.getCreatedAt());
        assertTrue(comment.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(comment.getCreatedAt().isAfter(LocalDateTime.now().minusSeconds(1)));
    }

    @Test
    void constructor_withNullFeedbackId_shouldThrow() {
        // Arrange
        UUID authorId = UUID.randomUUID();
        String content = "Test-Kommentar";

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> new Comment(null, authorId, content));
    }

    @Test
    void constructor_withNullAuthorId_shouldThrow() {
        // Arrange
        Long feedbackId = 1L;
        String content = "Test-Kommentar";

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> new Comment(feedbackId, null, content));
    }

    @Test
    void constructor_withNullContent_shouldThrow() {
        // Arrange
        Long feedbackId = 1L;
        UUID authorId = UUID.randomUUID();

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Comment(feedbackId, authorId, null));
    }

    @Test
    void constructor_withEmptyContent_shouldThrow() {
        // Arrange
        Long feedbackId = 1L;
        UUID authorId = UUID.randomUUID();

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Comment(feedbackId, authorId, ""));
        assertThrows(IllegalArgumentException.class,
                () -> new Comment(feedbackId, authorId, "   "));
    }

    @Test
    void constructor_withContentTooLong_shouldThrow() {
        // Arrange
        Long feedbackId = 1L;
        UUID authorId = UUID.randomUUID();
        String tooLongContent = "a".repeat(2001); // Mehr als 2000 Zeichen

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Comment(feedbackId, authorId, tooLongContent));
    }

    @Test
    void constructor_withMaxLengthContent_shouldSucceed() {
        // Arrange
        Long feedbackId = 1L;
        UUID authorId = UUID.randomUUID();
        String maxLengthContent = "a".repeat(2000); // Genau 2000 Zeichen

        // Act
        Comment comment = new Comment(feedbackId, authorId, maxLengthContent);

        // Assert
        assertEquals(maxLengthContent, comment.getContent());
    }

    @Test
    void setters_shouldUpdateFields() {
        // Arrange
        Comment comment = new Comment(1L, UUID.randomUUID(), "Original");
        Long newId = 2L;
        Long newFeedbackId = 3L;
        UUID newAuthorId = UUID.randomUUID();
        String newContent = "Updated";
        LocalDateTime newCreatedAt = LocalDateTime.now();

        // Act
        comment.setId(newId);
        comment.setFeedbackId(newFeedbackId);
        comment.setAuthorId(newAuthorId);
        comment.setContent(newContent);
        comment.setCreatedAt(newCreatedAt);

        // Assert
        assertEquals(newId, comment.getId());
        assertEquals(newFeedbackId, comment.getFeedbackId());
        assertEquals(newAuthorId, comment.getAuthorId());
        assertEquals(newContent, comment.getContent());
        assertEquals(newCreatedAt, comment.getCreatedAt());
    }

    @Test
    void equals_shouldCompareById() {
        // Arrange
        Long id = 1L;
        Comment comment1 = new Comment(1L, UUID.randomUUID(), "Content");
        comment1.setId(id);
        Comment comment2 = new Comment(2L, UUID.randomUUID(), "Different Content");
        comment2.setId(id);
        Comment comment3 = new Comment(1L, UUID.randomUUID(), "Content");
        comment3.setId(2L);

        // Act & Assert
        assertEquals(comment1, comment2); // Gleiche ID
        assertNotEquals(comment1, comment3); // Verschiedene IDs
    }

    @Test
    void hashCode_shouldBeConsistent() {
        // Arrange
        Comment comment = new Comment(1L, UUID.randomUUID(), "Content");
        comment.setId(1L);

        // Act
        int hashCode1 = comment.hashCode();
        int hashCode2 = comment.hashCode();

        // Assert
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void toString_shouldContainRelevantInformation() {
        // Arrange
        Long id = 1L;
        Long feedbackId = 2L;
        UUID authorId = UUID.randomUUID();
        String content = "Test Content";
        Comment comment = new Comment(feedbackId, authorId, content);
        comment.setId(id);

        // Act
        String toString = comment.toString();

        // Assert
        assertTrue(toString.contains(String.valueOf(id)));
        assertTrue(toString.contains(String.valueOf(feedbackId)));
        assertTrue(toString.contains(authorId.toString()));
        assertTrue(toString.contains(content));
    }
}

