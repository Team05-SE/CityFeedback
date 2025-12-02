package com.example.cityfeedback.feedbackmanagement.application;

import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Category;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackDTOTest {

    @Test
    void dtoShouldStoreValuesCorrectly() {
        FeedbackDTO dto = new FeedbackDTO();

        UUID userId = UUID.randomUUID();
        dto.userId = userId;
        dto.title = "Test Title";
        dto.category = Category.UMWELT;
        dto.content = "Some content";

        assertEquals(userId, dto.userId);
        assertEquals("Test Title", dto.title);
        assertEquals(Category.UMWELT, dto.category);
        assertEquals("Some content", dto.content);
    }

    @Test
    void dtoShouldAllowNullValues() {
        FeedbackDTO dto = new FeedbackDTO();

        dto.userId = null;
        dto.title = null;
        dto.category = null;
        dto.content = null;

        assertNull(dto.userId);
        assertNull(dto.title);
        assertNull(dto.category);
        assertNull(dto.content);
    }

    @Test
    void dtoIsPureDataContainer() {
        // erwartet: keine Exceptions, keine Logik
        assertDoesNotThrow(() -> new FeedbackDTO());
    }
}
