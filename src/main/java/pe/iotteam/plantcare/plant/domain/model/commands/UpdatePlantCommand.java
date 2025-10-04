package pe.iotteam.plantcare.plant.domain.model.commands;

public record UpdatePlantCommand(
        Long plantId,
        String name,
        String type,
        String imgUrl,
        String bio,
        String location
) {}