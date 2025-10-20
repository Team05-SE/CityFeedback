package src;

/**
 * Eine einfache Testklasse für CI.
 */
public final class ExampleTest {

    private ExampleTest() { }

    public static void main(final String[] args) {
        testAddition();
        testHelloWorld();
    }

    // Test 1: einfache Addition
    private static void testAddition() {
        int sum = 2 + 2;
        if (sum != 4) {
            throw new AssertionError("Test failed: 2 + 2 != 4");
        }
        System.out.println("Test passed!");
    }

    // Test 2: Überprüfung der Zeichenkette
    private static void testHelloWorld() {
        String text = "Hallo Welt";
        if (!text.equals("Hallo Welt")) {
            throw new AssertionError("Test failed: Ausgabe ist nicht 'Hallo Welt'");
        }
        System.out.println("Hallo-Welt-Test passed!");
    }
}
