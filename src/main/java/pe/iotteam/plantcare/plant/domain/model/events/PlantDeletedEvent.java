package pe.iotteam.plantcare.plant.domain.model.events;

import java.util.UUID;

public record PlantDeletedEvent(UUID plantId) {}