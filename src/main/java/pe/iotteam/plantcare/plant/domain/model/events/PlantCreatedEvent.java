package pe.iotteam.plantcare.plant.domain.model.events;

import java.util.UUID;

public record PlantCreatedEvent(UUID plantId, UUID userId, String name, String type) {}