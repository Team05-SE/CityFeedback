package com.example.cityfeedback.usermanagement.application;

import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private TestRestTemplate rest;

    @Test
    void createUser_shouldReturn200() {
        UserDTO dto = new UserDTO();
        dto.email = "usercontroller@test.de";
        dto.password = "Abcdef12";
        dto.role = UserRole.CITIZEN;

        HttpEntity<UserDTO> entity = new HttpEntity<>(dto);

        ResponseEntity<User> response = rest.postForEntity("/user", entity, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());
    }
}
