package com.example.cityfeedback.usermanagement.application;

import com.example.cityfeedback.usermanagement.domain.events.UserRegisteredEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserRegisteredEventHandler {

    @EventListener
    public void handleUserRegistered(UserRegisteredEvent event) {
        // Hier kannst du auf das Event reagieren:
        // - Willkommens-E-Mail versenden
        // - Logging
        // - Analytics
        // - Benachrichtigungen an andere Bounded Contexts

        System.out.println("🎉 Neuer User registriert: " + event);

        // TODO: Implementiere deine Event-Logik hier
    }
}