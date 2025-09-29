package pe.iotteam.plantcare.auth.application.internal.eventhandlers;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pe.iotteam.plantcare.auth.domain.model.events.UserRegisteredEvent;

@Component
public class UserRegisteredEventHandler {

    @EventListener
    public void on(UserRegisteredEvent event) {
        // TODO: Lógica tras registro (ej. mandar email de bienvenida)
        System.out.println("Nuevo usuario registrado: " + event.email());
    }
}