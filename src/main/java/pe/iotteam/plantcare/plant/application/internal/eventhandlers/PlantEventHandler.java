package pe.iotteam.plantcare.plant.application.internal.eventhandlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pe.iotteam.plantcare.plant.domain.model.events.PlantCreatedEvent;
import pe.iotteam.plantcare.plant.domain.model.events.PlantDeletedEvent;
import pe.iotteam.plantcare.plant.domain.model.events.PlantUpdatedEvent;

/**
 * Event handler for plant-related domain events.
 * This class logs the events for auditing or debugging purposes.
 */
@Slf4j
@Component
public class PlantEventHandler {

    /**
     * Handles the {@link PlantCreatedEvent} by logging the creation of a new plant.
     * @param event The plant creation event.
     */
    @EventListener
    public void on(PlantCreatedEvent event) {
        log.info("Plant with ID {} was created.", event.plantId());
    }

    /**
     * Handles the {@link PlantUpdatedEvent} by logging the update of an existing plant.
     * @param event The plant update event.
     */
    @EventListener
    public void on(PlantUpdatedEvent event) {
        log.info("Plant with ID {} was updated.", event.plantId());
    }

    /**
     * Handles the {@link PlantDeletedEvent} by logging the deletion of a plant.
     * @param event The plant deletion event.
     */
    @EventListener
    public void on(PlantDeletedEvent event) {
        log.info("Plant with ID {} was deleted.", event.plantId());
    }
}
