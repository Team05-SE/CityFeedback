package com.example.cityfeedback.feedbackmanagement.domain.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

    @Test
    void enumShouldContainFourValues() {
        assertEquals(4, Status.values().length);
    }

    @Test
    void enumShouldContainCorrectValues() {
        Status[] values = Status.values();

        assertEquals(Status.OPEN, values[0]);
        assertEquals(Status.INPROGRESS, values[1]);
        assertEquals(Status.DONE, values[2]);
        assertEquals(Status.CLOSED, values[3]);
    }

    @Test
    void valueOfShouldReturnCorrectEnum() {
        assertEquals(Status.OPEN, Status.valueOf("OPEN"));
        assertEquals(Status.INPROGRESS, Status.valueOf("INPROGRESS"));
        assertEquals(Status.DONE, Status.valueOf("DONE"));
        assertEquals(Status.CLOSED, Status.valueOf("CLOSED"));
    }

    @Test
    void invalidEnumName_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> Status.valueOf("INVALID"));
    }

    @Test
    void ordinalPositionsShouldStayConsistent() {
        assertEquals(0, Status.OPEN.ordinal());
        assertEquals(1, Status.INPROGRESS.ordinal());
        assertEquals(2, Status.DONE.ordinal());
        assertEquals(3, Status.CLOSED.ordinal());
    }

    @Test
    void toStringShouldReturnExactName() {
        assertEquals("OPEN", Status.OPEN.toString());
        assertEquals("INPROGRESS", Status.INPROGRESS.toString());
        assertEquals("DONE", Status.DONE.toString());
        assertEquals("CLOSED", Status.CLOSED.toString());
    }
}
