package com.example.cityfeedback.usermanagement.application;

import com.example.cityfeedback.feedbackmanagement.application.FeedbackService;
import com.example.cityfeedback.feedbackmanagement.domain.model.Feedback;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Category;
import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.exceptions.UnauthorizedException;
import com.example.cityfeedback.usermanagement.domain.repositories.UserRepository;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@org.springframework.test.annotation.DirtiesContext(classMode = org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DemoDataServiceTest {

    @Autowired
    private DemoDataService demoDataService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FeedbackService feedbackService;

    private User adminUser;
    private static int userCounter = 0;

    @BeforeEach
    void setUp() {
        // Lösche alle Demo-Daten vor jedem Test (falls vorhanden)
        try {
            List<User> allUsers = userRepository.findAll();
            List<User> existingDemoUsers = allUsers.stream()
                    .filter(user -> {
                        String email = user.getEmail().getValue();
                        return email.startsWith("demo.") || email.contains("@example.com");
                    })
                    .filter(user -> user.getRole() != UserRole.ADMIN)
                    .toList();
            
            for (User demoUser : existingDemoUsers) {
                feedbackService.deleteFeedbacksByUserId(demoUser.getId());
                userRepository.delete(demoUser);
            }
        } catch (Exception e) {
            // Ignore - Demo-Daten könnten nicht existieren
        }
        
        // Erstelle Admin für Tests
        String uniqueEmail = "admin-demo" + (userCounter++) + "@test.de";
        adminUser = new User(new Email(uniqueEmail), new Password("Abcdef12"), UserRole.ADMIN);
        adminUser = userRepository.save(adminUser);
    }

    @Test
    void deleteAllDemoData_asAdmin_shouldDeleteDemoUsers() {
        // Arrange: Erstelle Demo-User (falls nicht bereits vorhanden)
        User demoUser1;
        Email email1 = new Email("demo.buerger1@example.com");
        if (userRepository.existsByEmail(email1)) {
            demoUser1 = userRepository.findByEmail(email1).orElseThrow();
        } else {
            demoUser1 = new User(email1, new Password("Demo123!"), UserRole.CITIZEN);
            demoUser1 = userRepository.save(demoUser1);
        }

        User demoUser2;
        Email email2 = new Email("demo.mitarbeiter1@stadt.de");
        if (userRepository.existsByEmail(email2)) {
            demoUser2 = userRepository.findByEmail(email2).orElseThrow();
        } else {
            demoUser2 = new User(email2, new Password("Demo123!"), UserRole.STAFF);
            demoUser2 = userRepository.save(demoUser2);
        }

        User normalUser = new User(new Email("normal@test.de"), new Password("Abcdef12"), UserRole.CITIZEN);
        normalUser = userRepository.save(normalUser);

        // Act
        int deletedCount = demoDataService.deleteAllDemoData(adminUser.getId());

        // Assert
        assertTrue(deletedCount >= 2);
        assertFalse(userRepository.findById(demoUser1.getId()).isPresent());
        assertFalse(userRepository.findById(demoUser2.getId()).isPresent());
        assertTrue(userRepository.findById(normalUser.getId()).isPresent()); // Normaler User sollte bleiben
    }

    @Test
    void deleteAllDemoData_shouldDeleteDemoUserFeedbacks() {
        // Arrange: Erstelle Demo-User mit Feedback (falls nicht bereits vorhanden)
        Email demoEmail = new Email("demo.buerger1@example.com");
        User demoUser;
        if (userRepository.existsByEmail(demoEmail)) {
            demoUser = userRepository.findByEmail(demoEmail).orElseThrow();
            // Lösche vorhandene Feedbacks
            feedbackService.deleteFeedbacksByUserId(demoUser.getId());
        } else {
            demoUser = new User(demoEmail, new Password("Demo123!"), UserRole.CITIZEN);
            demoUser = userRepository.save(demoUser);
        }

        // Erstelle Feedback für Demo-User
        com.example.cityfeedback.feedbackmanagement.application.FeedbackDTO dto = 
                new com.example.cityfeedback.feedbackmanagement.application.FeedbackDTO();
        dto.userId = demoUser.getId();
        dto.title = "Demo Feedback";
        dto.category = Category.VERKEHR;
        dto.content = "Demo Content";
        Feedback feedback = feedbackService.createFeedback(dto);

        Long feedbackId = feedback.getId();

        // Act
        demoDataService.deleteAllDemoData(adminUser.getId());

        // Assert
        assertThrows(com.example.cityfeedback.feedbackmanagement.domain.exceptions.FeedbackNotFoundException.class,
                () -> feedbackService.getFeedbackById(feedbackId));
    }

    @Test
    void deleteAllDemoData_shouldNotDeleteAdmin() {
        // Arrange: Erstelle Demo-Admin (sollte nicht gelöscht werden)
        User demoAdmin = new User(new Email("demo.admin@example.com"), new Password("Demo123!"), UserRole.ADMIN);
        demoAdmin = userRepository.save(demoAdmin);

        // Act
        demoDataService.deleteAllDemoData(adminUser.getId());

        // Assert: Demo-Admin sollte nicht gelöscht werden
        assertTrue(userRepository.findById(demoAdmin.getId()).isPresent());
    }

    @Test
    void deleteAllDemoData_asStaff_shouldThrow() {
        // Arrange
        User staffUser = new User(new Email("staff@test.de"), new Password("Abcdef12"), UserRole.STAFF);
        staffUser = userRepository.save(staffUser);
        final java.util.UUID staffUserId = staffUser.getId();

        // Act & Assert
        assertThrows(UnauthorizedException.class,
                () -> demoDataService.deleteAllDemoData(staffUserId));
    }

    @Test
    void deleteAllDemoData_withNoDemoUsers_shouldReturnZero() {
        // Arrange: Keine Demo-User vorhanden

        // Act
        int deletedCount = demoDataService.deleteAllDemoData(adminUser.getId());

        // Assert
        assertEquals(0, deletedCount);
    }

    @Test
    void deleteAllDemoData_shouldOnlyDeleteDemoUsers() {
        // Arrange
        User demoUser = new User(new Email("demo.user@example.com"), new Password("Demo123!"), UserRole.CITIZEN);
        demoUser = userRepository.save(demoUser);

        User normalUser1 = new User(new Email("normal1@test.de"), new Password("Abcdef12"), UserRole.CITIZEN);
        normalUser1 = userRepository.save(normalUser1);

        User normalUser2 = new User(new Email("normal2@stadt.de"), new Password("Abcdef12"), UserRole.STAFF);
        normalUser2 = userRepository.save(normalUser2);

        // Act
        int deletedCount = demoDataService.deleteAllDemoData(adminUser.getId());

        // Assert
        assertEquals(1, deletedCount);
        assertFalse(userRepository.findById(demoUser.getId()).isPresent());
        assertTrue(userRepository.findById(normalUser1.getId()).isPresent());
        assertTrue(userRepository.findById(normalUser2.getId()).isPresent());
    }
}

