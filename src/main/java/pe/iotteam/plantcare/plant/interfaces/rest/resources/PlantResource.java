package pe.iotteam.plantcare.plant.interfaces.rest.resources;

import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PlantResource(
        Long id,
        UUID userId,
        String name,
        String type,
        String imgUrl,
        String bio,
        String location,
        PlantStatus status,
        LocalDateTime lastWatered,
        LocalDateTime nextWatering,
        List<PlantMetricsResource> metrics,
        List<WateringLogResource> wateringLogs,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}