package com.example.cityfeedback;

import java.util.List;

public class FeedbackValidatorTest {
    private final FeedbackValidator validator = new FeedbackValidator();

    // ✅ Gemeinsame Variable für alle erlaubten Kategorien
    private static final List<String> ALLOWED_CATEGORIES = List.of(
            "Verkehr",
            "Umwelt",
            "Beleuchtung",
            "Vandalismus",
            "Verwaltung"
    );

    // -------------------------------------------------------------
    // Happy Path Tests
    // -------------------------------------------------------------
    @Nested
    @DisplayName("Happy Path Tests")
    class HappyPathTests {

        @Test
        @DisplayName("Valid feedback with category Verkehr")
        void testValidFeedbackVerkehr() {
            Feedback f = new Feedback("Schlagloch in Straße", "Verkehr");
            assertDoesNotThrow(() -> validator.validate(f));
        }

        @Test
        @DisplayName("Valid feedback with category Umwelt")
        void testValidFeedbackUmwelt() {
            Feedback f = new Feedback("Illegale Müllablage", "Umwelt");
            assertDoesNotThrow(() -> validator.validate(f));
        }

        @Test
        @DisplayName("Valid feedback with category Beleuchtung")
        void testValidFeedbackBeleuchtung() {
            Feedback f = new Feedback("Straßenlaterne defekt", "Beleuchtung");
            assertDoesNotThrow(() -> validator.validate(f));
        }

        @Test
        @DisplayName("Valid feedback with category Vandalismus")
        void testValidFeedbackVandalismus() {
            Feedback f = new Feedback("Beschmierte Bushaltestelle", "Vandalismus");
            assertDoesNotThrow(() -> validator.validate(f));
        }

        @Test
        @DisplayName("Valid feedback with category Verwaltung")
        void testValidFeedbackVerwaltung() {
            Feedback f = new Feedback("Lange Bearbeitungszeit", "Verwaltung");
            assertDoesNotThrow(() -> validator.validate(f));
        }
    }
}
