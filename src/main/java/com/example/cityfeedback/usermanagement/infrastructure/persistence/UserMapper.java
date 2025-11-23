package com.example.cityfeedback.usermanagement.infrastructure.persistence;

import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;

/**
 * Mapper zwischen Domain-Entity User und JPA-Entity UserJpaEntity.
 */
public class UserMapper {

    public static UserJpaEntity toJpa(User user) {
        UserJpaEntity jpaEntity = new UserJpaEntity();
        jpaEntity.setId(user.getId());
        jpaEntity.setEmail(EmailJpaEmbeddable.fromDomain(user.getEmail()));
        jpaEntity.setPassword(PasswordJpaEmbeddable.fromDomain(user.getPassword()));
        jpaEntity.setRole(user.getRole());
        return jpaEntity;
    }

    public static User toDomain(UserJpaEntity jpaEntity) {
        User user = new User(
                jpaEntity.getEmail().toDomain(),
                jpaEntity.getPassword().toDomain(),
                jpaEntity.getRole()
        );
        user.setId(jpaEntity.getId());
        return user;
    }
}

