package com.example.cityfeedback.usermanagement.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA Repository für UserEntity.
 * Diese Schnittstelle ist nur für die Persistierung zuständig.
 */
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    /**
     * Prüft, ob ein User mit der angegebenen E-Mail existiert.
     * 
     * @param email Die E-Mail-Adresse als String
     * @return true, wenn ein User mit dieser E-Mail existiert
     */
    boolean existsByEmail(String email);

    /**
     * Findet einen User anhand seiner E-Mail-Adresse.
     * 
     * @param email Die E-Mail-Adresse als String
     * @return Optional mit der gefundenen UserEntity oder leer
     */
    Optional<UserEntity> findByEmail(String email);
}

