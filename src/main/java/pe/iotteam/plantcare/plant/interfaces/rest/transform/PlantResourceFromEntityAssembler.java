package pe.iotteam.plantcare.plant.interfaces.rest.transform;

import pe.iotteam.plantcare.plant.domain.model.aggregates.Plant;
import pe.iotteam.plantcare.plant.interfaces.rest.resources.PlantResource;

public class PlantResourceFromEntityAssembler {
    public static PlantResource toResource(Plant plant) {
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
                plant.getCreatedAt(),
                plant.getUpdatedAt()
        );
    }
}