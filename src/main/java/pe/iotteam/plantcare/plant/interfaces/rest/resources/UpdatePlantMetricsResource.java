package pe.iotteam.plantcare.plant.interfaces.rest.resources;

public record UpdatePlantMetricsResource(
        Integer temperature,
        Double humidity,
        Integer light,
        Integer soil_humidity
) {
}
