package com.example.cityfeedback.usermanagement.infrastructure.persistence;

import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;

/**
 * Mapper zwischen Domain-Modell (User) und Persistence-Entity (UserEntity).
 * Übersetzt zwischen der Domain-Schicht und der Datenbank-Schicht.
 */
public class UserMapper {

    /**
     * Konvertiert ein Domain-User-Objekt in eine Persistence-Entity.
     * 
     * @param user Domain-User-Objekt
     * @return UserEntity für die Persistierung
     */
    public static UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setEmail(user.getEmail().getValue());
        entity.setPassword(user.getPassword().getValue());
        entity.setRole(user.getRole());
        return entity;
    }

    /**
     * Konvertiert eine Persistence-Entity in ein Domain-User-Objekt.
     * 
     * @param entity UserEntity aus der Datenbank
     * @return Domain-User-Objekt
     */
    public static User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        // Achtung: Password wird als Hash gespeichert, daher müssen wir einen
        // speziellen Konstruktor verwenden, der den Hash direkt akzeptiert
        // Für jetzt verwenden wir den normalen Konstruktor, aber wir müssen
        // sicherstellen, dass Password einen Konstruktor für bereits gehashte Passwörter hat
        
        Email email = new Email(entity.getEmail());
        Password password = Password.fromHash(entity.getPassword());
        
        User user = new User(email, password, entity.getRole());
        // ID wird über Reflection oder einen Package-Private Setter gesetzt
        // Da User keine Setter für ID hat, müssen wir das über Reflection machen
        // Oder wir erweitern User um einen Package-Private Setter
        setUserId(user, entity.getId());
        
        return user;
    }

    /**
     * Setzt die ID eines User-Objekts.
     * Nutzt den Protected Setter.
     */
    private static void setUserId(User user, java.util.UUID id) {
        user.setId(id);
    }
}

