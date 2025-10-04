package pe.iotteam.plantcare.plant.interfaces.rest.transform;

import pe.iotteam.plantcare.plant.domain.model.commands.CreatePlantCommand;
import pe.iotteam.plantcare.plant.interfaces.rest.resources.CreatePlantResource;

public class CreatePlantCommandFromResourceAssembler {
    public static CreatePlantCommand toCommand(CreatePlantResource resource) {
        return new CreatePlantCommand(
                resource.getUserId(),
                resource.getName(),
                resource.getType(),
                resource.getImgUrl(),
                resource.getBio(),
                resource.getLocation()
        );
    }
}