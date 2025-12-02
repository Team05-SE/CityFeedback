package com.example.cityfeedback.usermanagement.domain.repositories;

import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository-Interface für User im Domain-Layer.
 * Definiert die Verträge für die Persistierung von User-Aggregaten.
 * 
 * Die Implementierung erfolgt in der Infrastructure-Schicht.
 */
public interface UserRepository {

    /**
     * Speichert einen User.
     * 
     * @param user Der zu speichernde User
     * @return Der gespeicherte User (mit generierter ID)
     */
    User save(User user);

    /**
     * Findet einen User anhand seiner ID.
     * 
     * @param id Die UUID des Users
     * @return Optional mit dem gefundenen User oder leer
     */
    Optional<User> findById(UUID id);

    /**
     * Findet alle Users.
     * 
     * @return Liste aller Users
     */
    List<User> findAll();

    /**
     * Prüft, ob ein User mit der angegebenen E-Mail existiert.
     * 
     * @param email Die E-Mail-Adresse
     * @return true, wenn ein User mit dieser E-Mail existiert
     */
    boolean existsByEmail(Email email);

    /**
     * Findet einen User anhand seiner E-Mail-Adresse.
     * 
     * @param email Die E-Mail-Adresse
     * @return Optional mit dem gefundenen User oder leer
     */
    Optional<User> findByEmail(Email email);

    /**
     * Löscht einen User.
     * 
     * @param user Der zu löschende User
     */
    void delete(User user);
}

