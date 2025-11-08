package com.example.cityfeedback;
// -------------------------------------------------------------
// NAME TESTS
// -------------------------------------------------------------
@Nested
@DisplayName("Name-Tests")
class NameTests {

    @Nested
    @DisplayName("Happy Path")
    class HappyPath {

        @Test
        @DisplayName("Name ist gültig (nicht null)")
        void validName() {
            assertDoesNotThrow(() -> validator.validateName("Peter"));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Name mit 1 Zeichen ist gültig")
        void nameOneCharacter() {
            assertDoesNotThrow(() -> validator.validateName("A"));
        }
    }

    @Nested
    @DisplayName("Negative Tests")
    class NegativeTests {

        @Test
        @DisplayName("Name ist null → Exception erwartet")
        void nameIsNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> validator.validateName(null));
        }
    }
}
