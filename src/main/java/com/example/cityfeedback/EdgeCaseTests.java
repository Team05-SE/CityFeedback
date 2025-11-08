package com.example.cityfeedback;
@Nested
@DisplayName("Edge-Case Tests")
class EdgeCaseTests {

    @Test
    @DisplayName("Name ist minimal gültig (1 Zeichen)")
    void testNameMinLength() {
        Feedback f = new Feedback("A", "Verkehr");
        assertDoesNotThrow(() -> validator.validate(f));
    }

    @Test
    @DisplayName("Kategorie mit führenden/trailenden Leerzeichen")
    void testCategoryWithWhitespace() {
        Feedback f = new Feedback("Lampe kaputt", "  Beleuchtung  ");
        assertDoesNotThrow(() -> validator.validate(f));
    }

    @Test
    @DisplayName("Name enthält Sonderzeichen")
    void testNameWithSpecialCharacters() {
        Feedback f = new Feedback("Lampe #12 defekt!", "Beleuchtung");
        assertDoesNotThrow(() -> validator.validate(f));
    }
}
