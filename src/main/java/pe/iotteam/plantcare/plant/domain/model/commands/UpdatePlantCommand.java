package pe.iotteam.plantcare.plant.domain.model.commands;

import java.util.UUID;

public record UpdatePlantCommand(
        UUID plantId,
        String name,
        String type,
        String imgUrl,
        String bio,
        String location
) {}