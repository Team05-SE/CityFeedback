package com.example.cityfeedback.usermanagement.infrastructure;

import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.repositories.UserRepository;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.infrastructure.persistence.UserEntity;
import com.example.cityfeedback.usermanagement.infrastructure.persistence.UserJpaRepository;
import com.example.cityfeedback.usermanagement.infrastructure.persistence.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementierung des UserRepository-Interfaces aus dem Domain-Layer.
 * Diese Klasse verbindet die Domain-Schicht mit der Persistierung.
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;

    public UserRepositoryImpl(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserMapper.toEntity(user);
        UserEntity savedEntity = jpaRepository.save(entity);
        return UserMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(UserMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll().stream()
                .map(UserMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.getValue());
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.getValue())
                .map(UserMapper::toDomain);
    }

    @Override
    public void delete(User user) {
        UserEntity entity = UserMapper.toEntity(user);
        jpaRepository.delete(entity);
    }
}

