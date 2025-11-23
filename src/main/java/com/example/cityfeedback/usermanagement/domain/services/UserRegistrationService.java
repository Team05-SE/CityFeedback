package com.example.cityfeedback.usermanagement.domain.services;

import com.example.cityfeedback.usermanagement.domain.exceptions.EmailAlreadyExistsException;
import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.repositories.UserRepository;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;

/**
 * Domain Service für Benutzerregistrierung.
 * Kapselt die Geschäftsregel: E-Mail muss eindeutig sein.
 */
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public UserRegistrationService(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    /**
     * Registriert einen neuen Benutzer.
     * 
     * @param email Die E-Mail-Adresse des Benutzers
     * @param rawPassword Das rohe Passwort (wird gehasht)
     * @return Der registrierte Benutzer
     * @throws EmailAlreadyExistsException wenn die E-Mail bereits existiert
     */
    public User registerUser(Email email, String rawPassword) {
        // Geschäftsregel: E-Mail muss eindeutig sein
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email.getValue());
        }

        // Passwort validieren und hashen
        Password.validatePassword(rawPassword);
        Password password = Password.create(rawPassword, passwordHasher);

        // Benutzer erstellen (ohne ID, wird beim Speichern gesetzt)
        User user = User.register(email, password);
        
        // Speichern (setzt die ID)
        userRepository.save(user);

        return user;
    }
}

