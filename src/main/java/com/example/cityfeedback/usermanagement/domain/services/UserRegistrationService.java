package com.example.cityfeedback.usermanagement.domain.services;

import com.example.cityfeedback.usermanagement.domain.exceptions.EmailAlreadyExistsException;
import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.repositories.UserRepository;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;

/**
 * Domain Service für die Benutzer-Registrierung.
 * Enthält Business-Logik, die über die Grenzen eines einzelnen Aggregats hinausgeht.
 * 
 * In diesem Fall: Die Prüfung, ob eine E-Mail bereits existiert, erfordert Zugriff
 * auf das Repository, was außerhalb der Verantwortlichkeit des User-Aggregats liegt.
 */
public class UserRegistrationService {

    private final UserRepository userRepository;

    public UserRegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registriert einen neuen Benutzer.
     * 
     * @param email E-Mail-Adresse des neuen Benutzers
     * @param password Passwort des neuen Benutzers
     * @return Der neu registrierte Benutzer
     * @throws EmailAlreadyExistsException wenn die E-Mail bereits existiert
     */
    public User registerUser(Email email, Password password) {
        // Business-Regel: E-Mail muss eindeutig sein
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email.getValue());
        }

        // Factory-Methode des Aggregats verwenden
        User user = User.register(email, password);
        
        // Aggregat speichern
        return userRepository.save(user);
    }
}

