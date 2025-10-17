package src;

/**
 * Eine einfache Testklasse für CI.
 */
public final class ExampleTest {

    private ExampleTest() { }

    public static void main(final String[] args) {
        int sum = 2 + 2;
        if (sum != 4) {
            throw new AssertionError("Test failed: 2 + 2 != 4");
        }
        System.out.println("Test passed!");
    }
}

