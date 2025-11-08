package com.example.cityfeedback;
@Nested
@DisplayName("Negative Tests")
class NegativeTests {

    @Test
    @DisplayName("Name ist null")
    void testNameNull() {
        Feedback f = new Feedback(null, "Verkehr");
        assertThrows(IllegalArgumentException.class, () -> validator.validate(f));
    }

    @Test
    @DisplayName("Name ist leer")
    void testNameEmpty() {
        Feedback f = new Feedback("", "Umwelt");
        assertThrows(IllegalArgumentException.class, () -> validator.validate(f));
    }

    @Test
    @DisplayName("Name enthält nur Whitespaces")
    void testNameWhitespace() {
        Feedback f = new Feedback("   ", "Vandalismus");
        assertThrows(IllegalArgumentException.class, () -> validator.validate(f));
    }

    @Test
    @DisplayName("Kategorie ist null")
    void testCategoryNull() {
        Feedback f = new Feedback("Müllproblem", null);
        assertThrows(IllegalArgumentException.class, () -> validator.validate(f));
    }

    @Test
    @DisplayName("Kategorie ist leer")
    void testCategoryEmpty() {
        Feedback f = new Feedback("Lampe kaputt", "");
        assertThrows(IllegalArgumentException.class, () -> validator.validate(f));
    }

    @Test
    @DisplayName("Kategorie nicht in erlaubter Liste")
    void testCategoryInvalid() {
        Feedback f = new Feedback("Lampe kaputt", "Energie");
        assertThrows(IllegalArgumentException.class, () -> validator.validate(f));
    }
}

