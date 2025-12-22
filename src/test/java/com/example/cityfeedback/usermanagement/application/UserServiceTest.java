package com.example.cityfeedback.usermanagement.application;

import com.example.cityfeedback.usermanagement.domain.exceptions.UserNotFoundException;
import com.example.cityfeedback.usermanagement.domain.exceptions.UnauthorizedException;
import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import com.example.cityfeedback.usermanagement.domain.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void createUser_shouldPersistUser() {
        Email email = new Email("example@mail.com");
        Password pw = new Password("Abcdef12");

        User user = userService.createUser(email, pw, UserRole.CITIZEN);

        assertNotNull(user.getId());
        assertEquals(email, user.getEmail());
    }

    @Test
    void getUserById_shouldReturnUser() {
        User user = userRepository.save(new User(new Email("aa@mail.de"), new Password("Abcdef12"), UserRole.ADMIN));

        User found = userService.getUserById(user.getId());

        assertEquals(user.getId(), found.getId());
    }

    @Test
    void getUserById_notExisting_shouldThrow() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(UUID.randomUUID()));
    }

    // ===================================================================
    // Admin-Funktionalitäten Tests
    // ===================================================================

    @Test
    void createUserByAdmin_asAdmin_shouldCreateUser() {
        // Arrange
        User admin = new User(new Email("admin@test.de"), new Password("Abcdef12"), UserRole.ADMIN);
        admin = userRepository.save(admin);

        Email newUserEmail = new Email("newuser@test.de");
        Password newUserPassword = new Password("Abcdef12");

        // Act
        User newUser = userService.createUserByAdmin(admin.getId(), newUserEmail, newUserPassword, UserRole.CITIZEN);

        // Assert
        assertNotNull(newUser.getId());
        assertEquals(newUserEmail, newUser.getEmail());
        assertEquals(UserRole.CITIZEN, newUser.getRole());
    }

    @Test
    void createUserByAdmin_asStaff_shouldThrow() {
        // Arrange
        User staff = new User(new Email("staff@test.de"), new Password("Abcdef12"), UserRole.STAFF);
        staff = userRepository.save(staff);

        Email newUserEmail = new Email("newuser@test.de");
        Password newUserPassword = new Password("Abcdef12");

        // Act & Assert
        assertThrows(UnauthorizedException.class,
                () -> userService.createUserByAdmin(staff.getId(), newUserEmail, newUserPassword, UserRole.CITIZEN));
    }

    @Test
    void updateRole_asAdmin_shouldChangeRole() {
        // Arrange
        User admin = new User(new Email("admin@test.de"), new Password("Abcdef12"), UserRole.ADMIN);
        admin = userRepository.save(admin);

        User user = new User(new Email("user@test.de"), new Password("Abcdef12"), UserRole.CITIZEN);
        user = userRepository.save(user);

        // Act
        User updated = userService.updateRole(admin.getId(), user.getId(), UserRole.STAFF);

        // Assert
        assertEquals(UserRole.STAFF, updated.getRole());
    }

    @Test
    void updateRole_asStaff_shouldThrow() {
        // Arrange
        User staff = new User(new Email("staff@test.de"), new Password("Abcdef12"), UserRole.STAFF);
        staff = userRepository.save(staff);
        final UUID staffId = staff.getId();

        User user = new User(new Email("user@test.de"), new Password("Abcdef12"), UserRole.CITIZEN);
        user = userRepository.save(user);
        final UUID userId = user.getId();

        // Act & Assert
        assertThrows(UnauthorizedException.class,
                () -> userService.updateRole(staffId, userId, UserRole.STAFF));
    }

    @Test
    void updatePassword_shouldChangePassword() {
        // Arrange
        User user = new User(new Email("user@test.de"), new Password("OldPass12"), UserRole.CITIZEN);
        user = userRepository.save(user);

        Password newPassword = new Password("NewPass12");

        // Act
        User updated = userService.updatePassword(user.getId(), newPassword);

        // Assert
        assertTrue(updated.getPassword().matches("NewPass12"));
    }

    @Test
    void updatePassword_withNonExistentUser_shouldThrow() {
        // Arrange
        Password newPassword = new Password("NewPass12");

        // Act & Assert
        assertThrows(UserNotFoundException.class,
                () -> userService.updatePassword(UUID.randomUUID(), newPassword));
    }

    @Test
    void deleteUser_asAdmin_shouldDeleteUserAndFeedbacks() {
        // Arrange
        User admin = new User(new Email("admin@test.de"), new Password("Abcdef12"), UserRole.ADMIN);
        admin = userRepository.save(admin);

        User userToDelete = new User(new Email("todelete@test.de"), new Password("Abcdef12"), UserRole.CITIZEN);
        userToDelete = userRepository.save(userToDelete);

        UUID userIdToDelete = userToDelete.getId();

        // Act
        userService.deleteUser(admin.getId(), userIdToDelete);

        // Assert
        assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(userIdToDelete));
    }

    @Test
    void deleteUser_asStaff_shouldThrow() {
        // Arrange
        User staff = new User(new Email("staff@test.de"), new Password("Abcdef12"), UserRole.STAFF);
        staff = userRepository.save(staff);
        final UUID staffId = staff.getId();

        User userToDelete = new User(new Email("todelete@test.de"), new Password("Abcdef12"), UserRole.CITIZEN);
        userToDelete = userRepository.save(userToDelete);
        final UUID userIdToDelete = userToDelete.getId();

        // Act & Assert
        assertThrows(UnauthorizedException.class,
                () -> userService.deleteUser(staffId, userIdToDelete));
    }

    @Test
    void deleteUser_adminDeletingSelf_shouldThrow() {
        // Arrange
        User adminUser = new User(new Email("admin@test.de"), new Password("Abcdef12"), UserRole.ADMIN);
        adminUser = userRepository.save(adminUser);

        final UUID adminId = adminUser.getId();

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> userService.deleteUser(adminId, adminId));
    }
}
