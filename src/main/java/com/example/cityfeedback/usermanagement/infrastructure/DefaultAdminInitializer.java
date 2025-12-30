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

import java.security.SecureRandom;

/**
 * Initialisiert den Default-Admin beim Start der Anwendung.
 * Erstellt einen Admin-User mit zufällig generiertem Passwort, falls noch kein Admin existiert.
 */
@Component
public class DefaultAdminInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DefaultAdminInitializer.class);
    
    private static final String DEFAULT_ADMIN_EMAIL = "admin@cityfeedback.de";
    private static final SecureRandom random = new SecureRandom();

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
            String generatedPassword = generateRandomPassword();
            Password adminPassword = new Password(generatedPassword);
            User admin = new User(adminEmail, adminPassword, UserRole.ADMIN);
            User savedAdmin = userRepository.save(admin);
            
            logger.info("✅ Default-Admin erfolgreich erstellt:");
            logger.info("   Email: {}", DEFAULT_ADMIN_EMAIL);
            logger.info("   Generiertes Passwort: {}", generatedPassword);
            logger.info("   ID: {}", savedAdmin.getId());
            logger.info("   ⚠️  Bitte ändern Sie das Passwort nach dem ersten Login!");
        } catch (Exception e) {
            logger.error("Fehler beim Erstellen des Default-Admins: {}", e.getMessage(), e);
        }
    }

    /**
     * Generiert ein sicheres zufälliges Passwort, das die Anforderungen erfüllt:
     * - Mindestens 8 Zeichen
     * - Mindestens ein Buchstabe
     * - Mindestens eine Zahl
     */
    private String generateRandomPassword() {
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialChars = "!@#$%^&*";
        String allChars = upperCase + lowerCase + digits + specialChars;
        
        StringBuilder password = new StringBuilder();
        
        // Mindestens ein Großbuchstabe
        password.append(upperCase.charAt(random.nextInt(upperCase.length())));
        
        // Mindestens ein Kleinbuchstabe
        password.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
        
        // Mindestens eine Zahl
        password.append(digits.charAt(random.nextInt(digits.length())));
        
        // Mindestens ein Sonderzeichen
        password.append(specialChars.charAt(random.nextInt(specialChars.length())));
        
        // Rest auf mindestens 12 Zeichen auffüllen (für mehr Sicherheit)
        int remainingLength = 8 + random.nextInt(5); // 8-12 Zeichen insgesamt
        for (int i = password.length(); i < remainingLength; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        
        // Zeichen mischen für mehr Zufälligkeit
        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }
        
        return new String(passwordArray);
    }
}

