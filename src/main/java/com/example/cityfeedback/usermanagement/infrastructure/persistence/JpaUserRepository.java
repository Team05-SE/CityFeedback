package com.example.cityfeedback.usermanagement.infrastructure.persistence;

import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.repositories.UserRepository;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Spring Data JPA Repository für UserJpaEntity.
 */
interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
    boolean existsByEmail_Value(String emailValue);
    Optional<UserJpaEntity> findByEmail_Value(String emailValue);
}

/**
 * Infrastructure-Implementierung des UserRepository-Interfaces.
 * Verwendet Spring Data JPA und mappt zwischen Domain- und JPA-Entities.
 */
@Repository
public class JpaUserRepository implements UserRepository {

    private final UserJpaRepository jpaRepository;

    public JpaUserRepository(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(User user) {
        if (user.getId() == null) {
            // Neuer User - erstelle neue JPA-Entity
            UserJpaEntity jpaEntity = UserMapper.toJpa(user);
            jpaEntity = jpaRepository.save(jpaEntity);
            user.setId(jpaEntity.getId());
        } else {
            // Existierender User - prüfe ob er existiert
            Optional<UserJpaEntity> existing = jpaRepository.findById(user.getId());
            if (existing.isPresent()) {
                // Update existierende Entity (Entity ist bereits managed)
                UserJpaEntity jpaEntity = existing.get();
                jpaEntity.setEmail(EmailJpaEmbeddable.fromDomain(user.getEmail()));
                jpaEntity.setPassword(PasswordJpaEmbeddable.fromDomain(user.getPassword()));
                jpaEntity.setRole(user.getRole());
                // Entity ist bereits managed, kein explizites save() nötig
                // Änderungen werden automatisch beim Transaction-Commit gespeichert
            } else {
                // User mit ID existiert nicht - speichere als neuen (ID wird ignoriert)
                UserJpaEntity jpaEntity = UserMapper.toJpa(user);
                jpaEntity.setId(null); // ID zurücksetzen, damit neue ID generiert wird
                jpaEntity = jpaRepository.save(jpaEntity);
                user.setId(jpaEntity.getId());
            }
        }
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
        return jpaRepository.existsByEmail_Value(email.getValue());
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaRepository.findByEmail_Value(email.getValue())
                .map(UserMapper::toDomain);
    }
}

