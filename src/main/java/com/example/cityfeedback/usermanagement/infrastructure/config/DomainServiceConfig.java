package com.example.cityfeedback.usermanagement.infrastructure.config;

import com.example.cityfeedback.usermanagement.domain.repositories.UserRepository;
import com.example.cityfeedback.usermanagement.domain.services.UserRegistrationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Konfiguration für Domain Services.
 * Erstellt Spring Beans für Domain Services, damit sie von Application Services verwendet werden können.
 */
@Configuration
public class DomainServiceConfig {

    @Bean
    public UserRegistrationService userRegistrationService(UserRepository userRepository) {
        return new UserRegistrationService(userRepository);
    }
}

