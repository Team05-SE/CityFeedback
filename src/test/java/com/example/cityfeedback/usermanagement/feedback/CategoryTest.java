package com.example.cityfeedback.usermanagement.feedback;

import com.example.cityfeedback.feedbackmanagement.domain.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    //Happy Path Test
    @Test
    @DisplayName("Enum enthält alle erwarteten Kategorien")
    void allCategoriesExist() {
        assertEquals(5, Category.values().length);

        assertNotNull(Category.valueOf("VERKEHR"));
        assertNotNull(Category.valueOf("UMWELT"));
        assertNotNull(Category.valueOf("BELEUCHTUNG"));
        assertNotNull(Category.valueOf("VANDALISMUS"));
        assertNotNull(Category.valueOf("VERWALTUNG"));
    }

    //Edge Case Test
    @Test
    @DisplayName("Enum enthält genau 5 Werte")
    void countIsCorrect() {
        assertEquals(5, Category.values().length);
    }

    //Negative Test
    @Test
    @DisplayName("valueOf wirft Exception bei ungültigem Wert")
    void invalidCategory() {
        assertThrows(IllegalArgumentException.class,
                () -> Category.valueOf("SONSTIGES"));
    }
}
