# Advanced Java, Test-Driven-Design (TDD) und LLM-gestütztes Entwickeln
## 1 Review Ihrer Implementierungsstrategie:

Ausgangsstrategie: Spring-Boot-Projekt, Domain-Model mit Entitäten (Bürger/Mitarbeiter/Admin), Aggregat User, Services & Repositories; Validierung bei Registrierung.

LLM-Verbesserungsvorschläge & Bewertung:
1. Value Objects für Email und Password statt primitiver Strings; Validierung und Normalisierung dort kapseln. → Übernommen: erhöht Domain-Klarheit, Wiederverwendbarkeit, Testbarkeit.
2. Registrierung zuerst als reine Domänenlogik (Factory im Aggregat), Application-/Infra-Schichten später anschließen. → Übernommen: passt zu TDD und hält Domain framework-unabhängig.
3. Rollen via separatem Policy-/Authorization-Service steuern statt in der Entität. → Abgelehnt für den aktuellen Projektumfang: unnötige Komplexität, Rollen als Enum im User ausreichend.


Kurzfazit (5–6 Sätze): Wir haben die Implementierungsstrategie mithilfe eines LLMs geschärft. Insbesondere die Einführung von Email und Password als Value Objects wurde übernommen, um Validierung und Normalisierung zentral abzubilden. Ebenso wurde die Empfehlung befolgt, die Registrierung zunächst als reine Domänenlogik innerhalb des Aggregats umzusetzen und die Infrastruktur erst später anzubinden. Ein Vorschlag, die Rollenverwaltung in einen separaten Policy-Service auszulagern, wurde aus Komplexitätsgründen verworfen. Die angepasste Strategie stärkt die Unabhängigkeit des Domain-Layers und erleichtert TDD. Dadurch ist die Basis gelegt, um in kleinen Schritten stabil weiterzuarbeiten.

 


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

### Aufgabe 2.5: Finale Implementierung:
Die finale Implementierung wurde im Code vorgenommen.

## 4.3 Implementierung der Domänenlogik (TDD Schritt 2) mit LLM-Pair-Programming
Umgesetzte Domain-Bausteine:
- Value Objects: Email, Password (inkl. Validierung/Normalisierung).  - Aggregate Root: User (Factory register, Id = UUID, Rolle = CITIZEN).  - Domain Service: UserRegistrationService mit Regel Email muss eindeutig sein.  - Repository-Vertrag: UserRepository (Interface, später durch Infra implementiert).

wichtige Implementierungsentscheidungen:
- Email: zuerst strip + lowercase, danach Regex-Validierung → robust gegen Whitespace.  - Password: keine Normalisierung (Sicherheit), Validierung über chars().anyMatch(Character::isLetter/Digit).  - User: reine Domänenfabrik, keine Infrastruktur-Abhängigkeiten.  - E-Mail-Eindeutigkeit: im Domain Service geprüft (existsByEmail), Aggregat bleibt zustandsorientiert.

Pair-Programming mit LLM (10–12 Sätze): Wir nutzten das LLM, um zuerst die Testfälle zu strukturieren und anschließend minimalen Code zu erzeugen, der die Tests erfüllt. Der Prozess war iterativ: Nach jedem roten Test wurden gezielt Anpassungen im Domain-Code vorgenommen, bis die Tests grün waren. Das LLM half, Regex-Varianten für E-Mail zu prüfen und die Passwortregeln präzise umzusetzen. Ein wichtiges Detail war die Reihenfolge der Normalisierung: Anfangs prüften wir die Regex vor dem strip(), was beim Test mit führenden/nachgestellten Leerzeichen scheiterte; das LLM unterstützte bei der Ursachenanalyse und beim Fix. Darüber hinaus empfahl das LLM, die E-Mail intern in Kleinschreibung zu speichern, was die Eindeutigkeitsprüfung vereinfacht. Für die E-Mail-Unique-Regel wurde ein UserRegistrationService eingeführt, der über ein Repository-Interface gegen eine In-Memory-Implementierung getestet wurde; so blieb der Domain-Layer frameworkfrei. Wir stellten sicher, dass der User keine Spring-Annotationen enthält und nur mit Value Objects interagiert. Aus Gründen der Verständlichkeit verzichteten wir in dieser Phase auf generische Abstraktionen oder Policies. Das LLM war hilfreich beim Entwurf verständlicher Testnamen und beim Aufräumen redundanter Tests. Insgesamt führte das Pair-Programming zu gutem, lesbarem Domain-Code mit klarer Verantwortlichkeit.

## 4.4 Tests-Erweiterung und Refaktorisierung (TDD Schritt 3):
Zusätzliche Edge-Cases:  - E-Mail: Groß-/Kleinschreibung → Normalisierung, Whitespace an Rändern, Plus-Alias, Subdomains.  - Passwort: Nur Sonderzeichen (negativ), Umlaute (positiv), exakt 8 Zeichen.

Refactorings:  - Email: Reihenfolge geändert → erst strip().toLowerCase(), dann Regex prüfen.  - trim() → strip() (Unicode-sicher).  - Testredundanzen entfernt, Namen präzisiert.

Dokumentation (5–6 Sätze):  Im Zuge der Tests-Erweiterung identifizierten wir Eingabevarianten, die in der Praxis häufig auftreten (Whitespace, Aliase, Subdomains). Die Fehlerursache beim gescheiterten Whitespace-Test lag in der falschen Reihenfolge von Normalisierung und Validierung; nach dem Refactoring wurden alle Tests grün. Der Wechsel von trim() auf strip() verbessert die Unicode-Behandlung. Redundante Negativtests wurden zusammengefasst, um die Wartbarkeit zu erhöhen. Nach jedem Refactoring wurde die komplette Testsuite ausgeführt, wodurch die Stabilität des Domain-Codes sichergestellt ist.

## 4.5 Modularität und Testbarkeit via CI/CD sicherstellen: 

+++ Inhalt noch einfügen +++

## 4.6 Kritische Reflektion zu TDD, DDD und LLM-gestützte Entwicklung:

+++ Inhalt noch einfügen +++





