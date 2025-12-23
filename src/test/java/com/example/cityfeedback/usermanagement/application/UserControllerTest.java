package com.example.cityfeedback.usermanagement.application;

import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import com.example.cityfeedback.usermanagement.domain.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private UserRepository userRepository;

    private User adminUser;
    private static int userCounter = 0;

    @BeforeEach
    void setUp() {
        // Erstelle Admin für Tests
        String uniqueEmail = "admin-controller" + (userCounter++) + "@test.de";
        adminUser = new User(new Email(uniqueEmail), new Password("Abcdef12"), UserRole.ADMIN);
        adminUser = userRepository.save(adminUser);
    }

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

    @Test
    void createUserByAdmin_shouldReturn200() {
        // Arrange
        CreateUserByAdminDTO dto = new CreateUserByAdminDTO();
        dto.email = "newuser@test.de";
        dto.password = "Abcdef12";
        dto.role = UserRole.CITIZEN;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Admin-Id", adminUser.getId().toString());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateUserByAdminDTO> request = new HttpEntity<>(dto, headers);

        // Act
        ResponseEntity<User> response = rest.postForEntity("/user/admin/create", request, User.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        assertEquals(dto.email, response.getBody().getEmail().getValue());
    }

    @Test
    void updateRole_shouldReturn200() {
        // Arrange
        User user = new User(new Email("user@test.de"), new Password("Abcdef12"), UserRole.CITIZEN);
        user = userRepository.save(user);

        ChangeRoleDTO dto = new ChangeRoleDTO();
        dto.role = UserRole.STAFF;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Admin-Id", adminUser.getId().toString());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ChangeRoleDTO> request = new HttpEntity<>(dto, headers);

        // Act
        ResponseEntity<User> response = rest.exchange(
                "/user/" + user.getId() + "/role",
                HttpMethod.PUT,
                request,
                User.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(UserRole.STAFF, response.getBody().getRole());
    }

    @Test
    void updatePassword_shouldReturn200() {
        // Arrange
        User user = new User(new Email("user@test.de"), new Password("OldPass12"), UserRole.CITIZEN);
        user = userRepository.save(user);

        ChangePasswordDTO dto = new ChangePasswordDTO();
        dto.password = "NewPass12";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Admin-Id", adminUser.getId().toString());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ChangePasswordDTO> request = new HttpEntity<>(dto, headers);

        // Act
        ResponseEntity<User> response = rest.exchange(
                "/user/" + user.getId() + "/password",
                HttpMethod.PUT,
                request,
                User.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Lade User neu aus Repository, um sicherzustellen, dass Passwort persistiert wurde
        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        // Passwort wurde geändert - prüfe ob es mit dem neuen Passwort übereinstimmt
        assertTrue(updatedUser.getPassword().matches("NewPass12"));
        // Prüfe dass es nicht mit dem alten Passwort übereinstimmt
        assertFalse(updatedUser.getPassword().matches("OldPass12"));
    }

    @Test
    void deleteUser_shouldReturn204() {
        // Arrange
        User userToDelete = new User(new Email("todelete@test.de"), new Password("Abcdef12"), UserRole.CITIZEN);
        userToDelete = userRepository.save(userToDelete);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Admin-Id", adminUser.getId().toString());
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<Void> response = rest.exchange(
                "/user/" + userToDelete.getId(),
                HttpMethod.DELETE,
                request,
                Void.class);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
