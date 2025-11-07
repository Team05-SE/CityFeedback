package com.example.cityfeedback.domain.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void validEmail_shouldBeAccepted() {
        assertDoesNotThrow(() -> new Email("user@mail.de"));
    }

    @Test
    void emailWithoutAtSymbol_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> new Email("usermail.de"));
    }

    @Test
    void emailWithoutTld_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> new Email("user@mail"));
    }

    @Test
    void emailWithPlusAlias_shouldBeValid() {
        assertDoesNotThrow(() -> new Email("user+test@mail.de"));
    }

    @Test
    void emailShouldBeCaseInsensitive() {
        Email email = new Email("USER@MAIL.DE");
        assertEquals("user@mail.de", email.getValue());
    }

    @Test
    void emailShouldTrimWhitespace() {
        Email email = new Email("   user@mail.de   ");
        assertEquals("user@mail.de", email.getValue());
    }
}