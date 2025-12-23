package com.example.cityfeedback.usermanagement.infrastructure;

import com.example.cityfeedback.feedbackmanagement.application.FeedbackService;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Category;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Status;
import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.repositories.UserRepository;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Initialisiert Demo-Daten beim Start der Anwendung.
 * Erstellt Beispiel-User und Feedbacks für Testzwecke.
 */
@Component
@Order(2) // Nach DefaultAdminInitializer ausführen
public class DemoDataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DemoDataInitializer.class);

    private final UserRepository userRepository;
    private final FeedbackService feedbackService;

    public DemoDataInitializer(UserRepository userRepository, FeedbackService feedbackService) {
        this.userRepository = userRepository;
        this.feedbackService = feedbackService;
    }

    @Override
    public void run(String... args) {
        // Prüfen ob Demo-Daten bereits existieren
        Email demoUserEmail = new Email("demo.buerger1@example.com");
        if (userRepository.existsByEmail(demoUserEmail)) {
            logger.info("Demo-Daten existieren bereits. Überspringe Initialisierung.");
            return;
        }

        logger.info("Erstelle Demo-Daten...");

        try {
            List<User> demoUsers = createDemoUsers();
            createDemoFeedbacks(demoUsers);
            
            logger.info("✅ Demo-Daten erfolgreich erstellt:");
            logger.info("   - {} Demo-User", demoUsers.size());
            logger.info("   - Demo-Feedbacks in verschiedenen Status");
        } catch (Exception e) {
            logger.error("Fehler beim Erstellen der Demo-Daten: {}", e.getMessage(), e);
        }
    }

    private List<User> createDemoUsers() {
        List<User> users = new ArrayList<>();

        // Demo-Bürger
        String[] citizenEmails = {
            "demo.buerger1@example.com",
            "demo.buerger2@example.com",
            "demo.buerger3@example.com",
            "demo.buerger4@example.com"
        };

        for (String email : citizenEmails) {
            try {
                Email emailVO = new Email(email);
                Password password = new Password("Demo123!");
                User user = new User(emailVO, password, UserRole.CITIZEN);
                User saved = userRepository.save(user);
                users.add(saved);
                logger.debug("Demo-Bürger erstellt: {}", email);
            } catch (Exception e) {
                logger.warn("Konnte Demo-Bürger {} nicht erstellen: {}", email, e.getMessage());
            }
        }

        // Demo-Mitarbeiter
        String[] staffEmails = {
            "demo.mitarbeiter1@stadt.de",
            "demo.mitarbeiter2@stadt.de"
        };

        for (String email : staffEmails) {
            try {
                Email emailVO = new Email(email);
                Password password = new Password("Demo123!");
                User user = new User(emailVO, password, UserRole.STAFF);
                User saved = userRepository.save(user);
                users.add(saved);
                logger.debug("Demo-Mitarbeiter erstellt: {}", email);
            } catch (Exception e) {
                logger.warn("Konnte Demo-Mitarbeiter {} nicht erstellen: {}", email, e.getMessage());
            }
        }

        return users;
    }

    private void createDemoFeedbacks(List<User> users) {
        if (users.isEmpty()) {
            logger.warn("Keine Demo-User vorhanden, kann keine Feedbacks erstellen.");
            return;
        }

        // Bürger-User (erste 4)
        List<User> citizens = users.stream()
                .filter(u -> u.getRole() == UserRole.CITIZEN)
                .limit(4)
                .toList();

        if (citizens.isEmpty()) {
            citizens = users;
        }

        // OPEN Feedbacks (werden direkt mit Status OPEN erstellt)
        createFeedback(citizens.get(0).getId(), "Müllcontainer überfüllt", Category.UMWELT,
            "Der Müllcontainer an der Ecke Hauptstraße/Neue Straße ist seit Tagen überfüllt. Der Müll quillt über und verursacht Gerüche.");
        createFeedback(citizens.get(1).getId(), "Straßenlaterne defekt", Category.BELEUCHTUNG,
            "Die Straßenlaterne vor Hausnummer 42 in der Musterstraße funktioniert nicht mehr. Es ist dort nachts sehr dunkel.");
        createFeedback(citizens.get(2).getId(), "Schlagloch in der Fahrbahn", Category.VERKEHR,
            "In der Bahnhofstraße, Höhe Hausnummer 15, befindet sich ein großes Schlagloch. Es ist gefährlich für Autofahrer und Radfahrer.");
        createFeedback(citizens.get(0).getId(), "Grafitti an der Bushaltestelle", Category.VANDALISMUS,
            "Die Bushaltestelle am Marktplatz wurde mit Graffiti besprüht. Die Wände sind vollständig beschmiert.");

        // OPEN Feedbacks (bereits mit Status OPEN)
        var openFeedback1 = createFeedback(citizens.get(1).getId(), "Baum beschädigt nach Sturm", Category.UMWELT,
            "Nach dem letzten Sturm ist ein großer Ast von der Eiche im Stadtpark abgebrochen. Der Ast liegt auf dem Gehweg.");

        var openFeedback2 = createFeedback(citizens.get(2).getId(), "Ampel zeigt dauerhaft rot", Category.VERKEHR,
            "Die Ampel an der Kreuzung Hauptstraße/Bahnhofstraße zeigt seit gestern dauerhaft rot. Der Verkehr staut sich erheblich.");

        // INPROGRESS Feedbacks
        var inProgressFeedback1 = createFeedback(citizens.get(3).getId(), "Spielplatzgerät defekt", Category.VERWALTUNG,
            "Die Schaukel auf dem Spielplatz am Park ist kaputt. Ein Seil ist gerissen.");
        if (inProgressFeedback1 != null) {
            try {
                feedbackService.updateFeedbackStatus(inProgressFeedback1.getId(), Status.INPROGRESS);
                feedbackService.publishFeedback(inProgressFeedback1.getId());
            } catch (Exception e) {
                logger.warn("Konnte Feedback nicht auf INPROGRESS setzen: {}", e.getMessage());
            }
        }

        var inProgressFeedback2 = createFeedback(citizens.get(0).getId(), "Weg überschwemmt", Category.VERKEHR,
            "Nach dem Regen ist der Fußweg am Bach überschwemmt. Man kann nicht mehr trockenen Fußes passieren.");
        if (inProgressFeedback2 != null) {
            try {
                feedbackService.updateFeedbackStatus(inProgressFeedback2.getId(), Status.INPROGRESS);
                feedbackService.publishFeedback(inProgressFeedback2.getId());
            } catch (Exception e) {
                logger.warn("Konnte Feedback nicht auf INPROGRESS setzen: {}", e.getMessage());
            }
        }

        // DONE Feedbacks
        var doneFeedback = createFeedback(citizens.get(1).getId(), "Straßenschild fehlt", Category.VERKEHR,
            "Das Straßenschild für die Neue Straße fehlt. Es wurde vermutlich bei einem Unfall beschädigt.");
        if (doneFeedback != null) {
            try {
                feedbackService.updateFeedbackStatus(doneFeedback.getId(), Status.DONE);
                feedbackService.publishFeedback(doneFeedback.getId());
            } catch (Exception e) {
                logger.warn("Konnte Feedback nicht auf DONE setzen: {}", e.getMessage());
            }
        }
    }

    private com.example.cityfeedback.feedbackmanagement.domain.model.Feedback createFeedback(
            UUID userId, String title, Category category, String content) {
        try {
            var dto = new com.example.cityfeedback.feedbackmanagement.application.FeedbackDTO();
            dto.userId = userId;
            dto.title = title;
            dto.category = category;
            dto.content = content;
            return feedbackService.createFeedback(dto);
        } catch (Exception e) {
            logger.warn("Konnte Feedback '{}' nicht erstellen: {}", title, e.getMessage());
            return null;
        }
    }
}

