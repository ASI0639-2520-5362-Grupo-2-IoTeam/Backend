package pe.iotteam.plantcare.plant.interfaces.rest.transform;

import pe.iotteam.plantcare.plant.domain.model.aggregates.PlantMetrics;
import pe.iotteam.plantcare.plant.interfaces.rest.resources.PlantMetricsResource;

public class PlantMetricsResourceFromEntityAssembler {
    public static PlantMetricsResource toResource(PlantMetrics metrics) {
        return new PlantMetricsResource(
                metrics.getId(),
                metrics.getPlantId().plantId(),
                metrics.getDeviceId(),
                metrics.getTemperature(),
                metrics.getHumidity(),
                metrics.getLight(),
                metrics.getSoilHumidity(),
                metrics.getCreatedAt()
        );
    }
}