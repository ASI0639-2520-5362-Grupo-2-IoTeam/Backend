package pe.iotteam.plantcare.plant.application.internal.eventhandlers;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pe.iotteam.plantcare.plant.domain.model.events.PlantCreatedEvent;
import pe.iotteam.plantcare.plant.domain.model.events.PlantUpdatedEvent;
import pe.iotteam.plantcare.plant.domain.model.events.PlantDeletedEvent;

@Component
public class PlantEventHandler {

    @EventListener
    public void on(PlantCreatedEvent event) {
        System.out.println("Plant created: " + event.plantId());
    }

    @EventListener
    public void on(PlantUpdatedEvent event) {
        System.out.println("Plant updated: " + event.plantId());
    }

    @EventListener
    public void on(PlantDeletedEvent event) {
        System.out.println("Plant deleted: " + event.plantId());
    }
}