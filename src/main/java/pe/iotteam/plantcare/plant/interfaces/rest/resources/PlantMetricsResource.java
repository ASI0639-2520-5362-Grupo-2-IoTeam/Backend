package pe.iotteam.plantcare.plant.interfaces.rest.resources;

import java.time.LocalDateTime;

public record PlantMetricsResource(
        Long id,
        Long plantId,
        String deviceId,
        Integer temperature,
        Integer humidity,
        Integer light,
        Integer soilHumidity,
        LocalDateTime createdAt
) {
}