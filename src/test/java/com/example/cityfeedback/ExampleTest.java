package com.example.cityfeedback;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Beispielhafte Tests.
 */
public class ExampleTest {

    @Test
    void testAddition() {
        assertEquals(4, 2 + 2, "2 + 2 sollte 4 ergeben");
    }

    @Test
    void testHelloWorld() {
        String text = "Hallo Welt";
        assertTrue(text.equals("Hallo Welt"), "Text sollte 'Hallo Welt' sein");
    }
}

