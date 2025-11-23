package com.example.cityfeedback.usermanagement.infrastructure.services;

import com.example.cityfeedback.usermanagement.domain.services.PasswordHasher;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

/**
 * BCrypt-Implementierung des PasswordHasher-Interfaces.
 * Infrastructure-Layer Implementierung.
 */
@Component
public class BcryptPasswordHasher implements PasswordHasher {

    @Override
    public String hash(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    @Override
    public boolean matches(String rawPassword, String hashedPassword) {
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }
}

