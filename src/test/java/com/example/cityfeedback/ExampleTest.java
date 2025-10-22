package com.example.cityfeedback;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Beispielhafte Tests für CityFeedback.
 */
public class ExampleTest {

    /**
     * Testet eine einfache Addition.
     */
    @Test
    void testAddition() {
        final int expected = 4;
        final int actual = 2 + 2;
        assertEquals(expected, actual, "2 + 2 sollte 4 ergeben");
    }

    /**
     * Testet eine einfache Zeichenkette.
     */
    @Test
    void testHelloWorld() {
        final String text = "Hallo Welt";
        assertTrue(text.equals("Hallo Welt"), "Text sollte 'Hallo Welt' sein");
    }
}

