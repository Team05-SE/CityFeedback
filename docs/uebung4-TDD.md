# Advanced Java, Test-Driven-Design (TDD) und LLM-gestütztes Entwickeln
## 1 Review Ihrer Implementierungsstrategie:
++ Inhalt noch hinzufügen ++


## 2 Testfälle mit LLM generieren und validieren (TDD Schritt 1):

### Aufgabe 2.1: Prompt für die LLM:
Ausgangspunkt ist eine Bürgerbeteiligungsplattform (City Feedback), die die Möglichkeit bietet, der Stadtverwaltung Feedback wie z.B. Mängel zu melden mit den Boundede Contexts User Management Context und Feedback Management Context. 
Bitte generiere dafür JUnit Tests, die die folgenden Regeln berücksichtigen: 
- Name: Darf nicht null sein
- Kategorie: Erlaubt sind die Katgorien Verkehr, Umwelt, Beleuchtung, Vandalismus, Verwaltung
Bitte erstelle dafür JUnit-Tests:
- Happy-Path-Tests
- Edge-Cases (Grenzfälle)
- Negative Tests (ungültige Eingaben)


### Aufgabe 2.2: Test Cases generieren lassen:
Die LLM (Chat GPT 5, Plus-Version) hat folgende Tests generiert: 

    class FeedbackValidatorTest {

    private final FeedbackValidator validator = new FeedbackValidator();

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

    // -------------------------------------------------------------
    // Edge Cases / Grenzfälle
    // -------------------------------------------------------------
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

    // -------------------------------------------------------------
    // Negative Tests (Ungültige Eingaben)
    // -------------------------------------------------------------
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
    }




### Aufgabe 2.3: Kritische Bewertung:
- Der Prompt wurde mehrere Male bei Chat GPT eingegeben und jedes Mal wurden unterschiedliche Tests zur Verfügung gestellt.
- Im Prompt wurde nur beschrieben, dass der Name nicht null sein darf. Chat GPT hat aber zusätzlich Testfälle generiert, die prüfen, dass der Name nicht nur ein Whitespace enthält oder leer ist. Dies ist zwar inhaltlich sinnvoll, war aber nicht gefordert. Zudem hat Chat GPT den Namen so gedeutet, dass dieser sich auf die Kategorie bezieht. Gemeint waren aber die Namen der Bürger - dies hätte im Prompt aber auch besser beschrieben werden können.
- Bei den Kategorien wird der Test für jede Kategorie einzeln wiederholt. Dies könnte man in einem Test zusammenfassen, wodurch weniger Code nötig wäre und dieser besser wartbar wäre.
  

### Aufgabe 2.4: Regex-Validierung:
- Chat GPT hat folgendes Regex für Namen vorgeschlagen:
  ^[A-Za-zÄÖÜäöüßÀ-ÖØ-öø-ÿ' -]{1,80}$
- Beschreibt einen Text aus 1 bis 80 Zeichen, der nur folgende Zeichen enthalten darf:
   - Lateinische Buchstaben A–Z und a–z
   - Umlaute und ß (Ä Ö Ü ä ö ü ß)
   - Weitere lateinische Buchstaben mit Akzenten (Bereiche À–Ö, Ø–ö, ø–ÿ)
   - Leerzeichen
   - Bindestrich (-)
   - Apostroph (')
   - Keine Ziffern, keine Sonderzeichen (wie !, ?, @, etc.), keine Zeilenumbrüche.
- Dieses Regex deckt bereits sehr viele Namen ab und kann daher gut verwenden. Man könnte 
  das Regex nicht optimieren, indem man weitere Sonderzeichen erlabt, um alle Namen 
  abzudecken.

### Übung 4, Aufgabe 2.5: Finale Implementierung:
Die finale Implementierung wurde im Code vorgenommen.

## Implementierung der Domänenlogik (TDD Schritt 2) mit LLM-Pair-Programming

+++ Inhalt noch hinzufügen +++


