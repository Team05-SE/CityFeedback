package com.example.cityfeedback.usermanagement.domain.services;

/**
 * Domain Service Interface für Passwort-Hashing.
 * Implementierung erfolgt im Infrastructure-Layer.
 */
public interface PasswordHasher {
    
    /**
     * Hasht ein rohes Passwort.
     * @param rawPassword Das rohe Passwort
     * @return Der Hash-Wert
     */
    String hash(String rawPassword);
    
    /**
     * Prüft, ob ein rohes Passwort mit einem Hash übereinstimmt.
     * @param rawPassword Das rohe Passwort
     * @param hashedPassword Der Hash-Wert
     * @return true wenn übereinstimmend
     */
    boolean matches(String rawPassword, String hashedPassword);
}

