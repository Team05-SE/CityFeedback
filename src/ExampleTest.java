public class ExampleTest {
    public static void main(String[] args) {
        int sum = 2 + 2;
        if (sum != 4) {
            throw new AssertionError("Test failed: 2 + 2 != 4");
        }
        System.out.println("Test passed!");
    }
}

