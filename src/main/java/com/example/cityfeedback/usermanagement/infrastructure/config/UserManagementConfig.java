package com.example.cityfeedback.usermanagement.infrastructure.config;

import com.example.cityfeedback.usermanagement.domain.repositories.UserRepository;
import com.example.cityfeedback.usermanagement.domain.services.PasswordHasher;
import com.example.cityfeedback.usermanagement.domain.services.UserRegistrationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring-Konfiguration für UserManagement-Bounded Context.
 * Registriert Domain Services als Beans.
 */
@Configuration
public class UserManagementConfig {

    @Bean
    public UserRegistrationService userRegistrationService(
            UserRepository userRepository,
            PasswordHasher passwordHasher) {
        return new UserRegistrationService(userRepository, passwordHasher);
    }
}

