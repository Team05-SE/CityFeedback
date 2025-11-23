package com.example.cityfeedback.usermanagement.application;

import com.example.cityfeedback.usermanagement.domain.events.UserRegisteredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Event Handler für UserRegisteredEvent.
 * Reagiert auf Benutzerregistrierungen und kann z.B.:
 * - Willkommens-E-Mail versenden
 * - Analytics-Events auslösen
 * - Benachrichtigungen an andere Bounded Contexts senden
 */
@Component
public class UserRegisteredEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(UserRegisteredEventHandler.class);

    @EventListener
    public void handleUserRegistered(UserRegisteredEvent event) {
        logger.info("New user registered: userId={}, email={}", 
            event.getUserId(), event.getEmail());
        
        // Weitere Event-Logik kann hier implementiert werden:
        // - E-Mail-Versand
        // - Analytics
        // - Benachrichtigungen
    }
}