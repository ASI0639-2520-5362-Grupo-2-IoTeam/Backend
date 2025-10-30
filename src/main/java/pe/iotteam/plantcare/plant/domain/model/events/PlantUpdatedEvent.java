package pe.iotteam.plantcare.plant.domain.model.events;

import java.util.UUID;

public record PlantUpdatedEvent(UUID plantId, String name, String type) {}