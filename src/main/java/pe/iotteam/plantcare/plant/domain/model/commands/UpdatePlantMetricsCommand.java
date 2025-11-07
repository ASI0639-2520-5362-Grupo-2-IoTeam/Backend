package pe.iotteam.plantcare.plant.domain.model.commands;

public record UpdatePlantMetricsCommand(
        Long id,
        Integer temperature,
        Double humidity,
        Integer light,
        Integer soil_humidity
) {
}
