package com.example.cityfeedback.feedbackmanagement.domain.model;

import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Category;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Status;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {

    @Test
    void create_shouldSetFieldsCorrectly() {
        UUID userId = UUID.randomUUID();
        Feedback feedback = Feedback.create(
                "Test",
                Category.UMWELT,
                "Content",
                userId
        );

        assertEquals("Test", feedback.getTitle());
        assertEquals("Content", feedback.getContent());
        assertEquals(Category.UMWELT, feedback.getCategory());
        assertEquals(Status.OPEN, feedback.getStatus());
        assertEquals(userId, feedback.getUserId());
        assertFalse(feedback.isPublished());
    }

    @Test
    void publish_shouldSetPublishedToTrue() {
        UUID userId = UUID.randomUUID();
        Feedback feedback = Feedback.create("Test", Category.VERKEHR, "Content", userId);
        
        feedback.publish();
        
        assertTrue(feedback.isPublished());
    }

    @Test
    void publish_whenAlreadyPublished_shouldThrow() {
        UUID userId = UUID.randomUUID();
        Feedback feedback = Feedback.create("Test", Category.VERKEHR, "Content", userId);
        feedback.publish();
        
        assertThrows(IllegalStateException.class, () -> feedback.publish());
    }

    @Test
    void updateStatus_shouldChangeStatus() {
        UUID userId = UUID.randomUUID();
        Feedback feedback = Feedback.create("Test", Category.VERKEHR, "Content", userId);
        
        feedback.updateStatus(Status.INPROGRESS);
        
        assertEquals(Status.INPROGRESS, feedback.getStatus());
    }

    @Test
    void close_shouldSetStatusToClosed() {
        UUID userId = UUID.randomUUID();
        Feedback feedback = Feedback.create("Test", Category.VERKEHR, "Content", userId);
        feedback.publish();
        
        feedback.close();
        
        assertEquals(Status.CLOSED, feedback.getStatus());
        assertFalse(feedback.isPublished()); // Geschlossene Feedbacks sind nicht veröffentlicht
    }

    @Test
    void create_withInvalidTitle_shouldThrow() {
        UUID userId = UUID.randomUUID();
        
        assertThrows(IllegalArgumentException.class, 
            () -> Feedback.create(null, Category.VERKEHR, "Content", userId));
        assertThrows(IllegalArgumentException.class, 
            () -> Feedback.create("", Category.VERKEHR, "Content", userId));
    }

    @Test
    void create_withInvalidContent_shouldThrow() {
        UUID userId = UUID.randomUUID();
        
        assertThrows(IllegalArgumentException.class, 
            () -> Feedback.create("Title", Category.VERKEHR, null, userId));
        assertThrows(IllegalArgumentException.class, 
            () -> Feedback.create("Title", Category.VERKEHR, "", userId));
    }
}
