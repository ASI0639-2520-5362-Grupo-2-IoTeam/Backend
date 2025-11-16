package pe.iotteam.plantcare.plant.interfaces.rest.transform;

import pe.iotteam.plantcare.plant.domain.model.commands.UpdatePlantMetricsCommand;
import pe.iotteam.plantcare.plant.interfaces.rest.resources.UpdatePlantMetricsResource;

public class UpdatePlantMetricsCommandFromResourceAssembler {
    public static UpdatePlantMetricsCommand toCommand(Long plantId, UpdatePlantMetricsResource resource) {
        return new UpdatePlantMetricsCommand(
                plantId,
                resource.airTemperatureCelsius(),
                resource.airHumidityPercent(),
                resource.luminosityLux(),
                resource.soilMoisturePercent()
        );
    }
}
