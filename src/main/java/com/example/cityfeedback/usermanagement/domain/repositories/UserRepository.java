package com.example.cityfeedback.usermanagement.domain.repositories;

import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Domain Repository Interface für User.
 * Definiert die benötigten Operationen unabhängig von der Persistenz-Technologie.
 */
public interface UserRepository {

    void save(User user);

    Optional<User> findById(UUID id);

    List<User> findAll();

    boolean existsByEmail(Email email);

    Optional<User> findByEmail(Email email);
}

