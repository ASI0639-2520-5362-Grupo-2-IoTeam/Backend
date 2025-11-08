package pe.iotteam.plantcare.plant.interfaces.rest.transform;

import pe.iotteam.plantcare.plant.domain.model.aggregates.Plant;
import pe.iotteam.plantcare.plant.interfaces.rest.resources.PlantWithTelemetryResource;

public class PlantWithTelemetryResourceFromEntityAssembler {
    public static PlantWithTelemetryResource toResource(Plant plant, Integer temperature, Double humidity, Integer light, Integer soil_humidity, String deviceId) {
        return new PlantWithTelemetryResource(
                plant.getId(),
                plant.getUserId().value(),
                plant.getName(),
                plant.getType(),
                plant.getImgUrl(),
                plant.getBio(),
                plant.getLocation(),
                plant.getStatus(),
                plant.getLastWatered(),
                plant.getNextWatering(),
                plant.getCreatedAt(),
                plant.getUpdatedAt(),
                temperature,
                humidity,
                light,
                soil_humidity,
                deviceId
        );
    }
}