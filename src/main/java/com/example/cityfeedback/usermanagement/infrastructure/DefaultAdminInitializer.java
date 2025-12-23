package com.example.cityfeedback.usermanagement.infrastructure;

import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.repositories.UserRepository;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Initialisiert den Default-Admin beim Start der Anwendung.
 * Erstellt einen Admin-User mit vordefinierten Credentials, falls noch kein Admin existiert.
 */
@Component
public class DefaultAdminInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DefaultAdminInitializer.class);
    
    private static final String DEFAULT_ADMIN_EMAIL = "admin@cityfeedback.de";
    private static final String DEFAULT_ADMIN_PASSWORD = "Admin123!";

    private final UserRepository userRepository;

    public DefaultAdminInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        // Prüfen, ob bereits ein Admin existiert
        Email adminEmail = new Email(DEFAULT_ADMIN_EMAIL);
        
        if (userRepository.existsByEmail(adminEmail)) {
            logger.info("Default-Admin existiert bereits: {}", DEFAULT_ADMIN_EMAIL);
            return;
        }

        // Default-Admin erstellen
        try {
            Password adminPassword = new Password(DEFAULT_ADMIN_PASSWORD);
            User admin = new User(adminEmail, adminPassword, UserRole.ADMIN);
            User savedAdmin = userRepository.save(admin);
            
            logger.info("✅ Default-Admin erfolgreich erstellt:");
            logger.info("   Email: {}", DEFAULT_ADMIN_EMAIL);
            logger.info("   Passwort: {}", DEFAULT_ADMIN_PASSWORD);
            logger.info("   ID: {}", savedAdmin.getId());
            logger.info("   ⚠️  Bitte ändern Sie das Passwort nach dem ersten Login!");
        } catch (Exception e) {
            logger.error("Fehler beim Erstellen des Default-Admins: {}", e.getMessage(), e);
        }
    }
}

