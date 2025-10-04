package pe.iotteam.plantcare.plant.interfaces.rest.transform;

import pe.iotteam.plantcare.plant.domain.model.commands.UpdatePlantCommand;
import pe.iotteam.plantcare.plant.interfaces.rest.resources.UpdatePlantResource;

import java.util.UUID;

public class UpdatePlantCommandFromResourceAssembler {
    public static UpdatePlantCommand toCommand(UUID plantId, UpdatePlantResource resource) {
        return new UpdatePlantCommand(
                plantId,
                resource.getName(),
                resource.getType(),
                resource.getImgUrl(),
                resource.getBio(),
                resource.getLocation()
        );
    }
}