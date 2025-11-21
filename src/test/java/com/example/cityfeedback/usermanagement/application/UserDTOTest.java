package com.example.cityfeedback.usermanagement.application;

import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    @Test
    void dtoShouldStoreValuesCorrectly() {
        UserDTO dto = new UserDTO();

        dto.email = "user@mail.de";
        dto.password = "Abcdef12";
        dto.role = UserRole.CITIZEN;

        assertEquals("user@mail.de", dto.email);
        assertEquals("Abcdef12", dto.password);
        assertEquals(UserRole.CITIZEN, dto.role);
    }

    @Test
    void dtoShouldAllowNullValues() {
        UserDTO dto = new UserDTO();

        dto.email = null;
        dto.password = null;
        dto.role = null;

        assertNull(dto.email);
        assertNull(dto.password);
        assertNull(dto.role);
    }

    @Test
    void dtoHasNoValidationLogic() {
        assertDoesNotThrow(UserDTO::new);
    }
}
