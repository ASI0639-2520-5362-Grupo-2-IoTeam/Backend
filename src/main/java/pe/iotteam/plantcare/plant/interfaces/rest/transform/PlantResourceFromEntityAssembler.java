package pe.iotteam.plantcare.plant.interfaces.rest.transform;

import pe.iotteam.plantcare.plant.domain.model.aggregates.Plant;
import pe.iotteam.plantcare.plant.interfaces.rest.resources.PlantResource;

import java.util.stream.Collectors;

public class PlantResourceFromEntityAssembler {
    public static PlantResource toResource(Plant plant) {
        var metrics = plant.getMetrics().stream()
                .map(PlantMetricsResourceFromEntityAssembler::toResource)
                .collect(Collectors.toList());
        var wateringLogs = plant.getWateringLogs().stream()
                .map(WateringLogResourceFromEntityAssembler::toResource)
                .collect(Collectors.toList());

        return new PlantResource(
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
                metrics,
                wateringLogs,
                plant.getCreatedAt(),
                plant.getUpdatedAt()
        );
    }
}