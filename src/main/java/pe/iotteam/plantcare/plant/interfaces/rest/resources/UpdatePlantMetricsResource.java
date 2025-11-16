package pe.iotteam.plantcare.plant.interfaces.rest.resources;

public record UpdatePlantMetricsResource(
        Integer airTemperatureCelsius,
        Double airHumidityPercent,
        Integer luminosityLux,
        Integer soilMoisturePercent
) {
}
