package pe.iotteam.plantcare.plant.interfaces.rest.transform;

import pe.iotteam.plantcare.plant.domain.model.aggregates.WateringLog;
import pe.iotteam.plantcare.plant.interfaces.rest.resources.WateringLogResource;

public class WateringLogResourceFromEntityAssembler {
    public static WateringLogResource toResource(WateringLog log) {
        return new WateringLogResource(
                log.getId(),
                log.getPlantId().plantId(),
                log.getWateredAt()
        );
    }
}