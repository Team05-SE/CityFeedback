package com.example.cityfeedback;
// -------------------------------------------------------------
// CATEGORY TESTS
// -------------------------------------------------------------
@Nested
@DisplayName("Kategorie-Tests")
class CategoryTests {
    @Nested
    @DisplayName("Happy Path")
    class HappyPath {

        @Test
        @DisplayName("Gültige Kategorie (z. B. Verkehr)")
        void validCategory() {
            assertDoesNotThrow(() ->
                    validator.validateCategory(ALLOWED_CATEGORIES.get(0)));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Alle erlaubten Kategorien sollten gültig sein")
        void allAllowedCategories() {
            for (String category : ALLOWED_CATEGORIES) {
                assertDoesNotThrow(() -> validator.validateCategory(category),
                        "Kategorie sollte gültig sein: " + category);
            }
        }
    }

    @Nested
    @DisplayName("Negative Tests")
    class NegativeTests {

        @Test
        @DisplayName("Kategorie nicht in erlaubter Liste → Exception erwartet")
        void categoryInvalid() {
            assertThrows(IllegalArgumentException.class,
                    () -> validator.validateCategory("Sonstiges"));
        }
    }
}
