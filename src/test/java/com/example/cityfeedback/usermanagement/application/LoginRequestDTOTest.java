package com.example.cityfeedback.usermanagement.application;

import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestDTOTest {

    @Test
    void dtoShouldStoreValuesCorrectly() {
        LoginRequestDTO dto = new LoginRequestDTO();

        dto.email = "abc@test.de";
        dto.password = "Passwort123";

        assertEquals("abc@test.de", dto.email);
        assertEquals("Passwort123", dto.password);
    }

    @Test
    void dtoShouldAllowNullValues() {
        LoginRequestDTO dto = new LoginRequestDTO();

        dto.email = null;
        dto.password = null;

        assertNull(dto.email);
        assertNull(dto.password);
    }

    @Test
    void dtoHasNoValidationLogic() {
        assertDoesNotThrow(LoginRequestDTO::new);
    }
}
