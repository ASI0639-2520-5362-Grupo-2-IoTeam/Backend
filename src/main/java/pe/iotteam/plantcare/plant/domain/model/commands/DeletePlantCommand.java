package pe.iotteam.plantcare.plant.domain.model.commands;

import java.util.UUID;

public record DeletePlantCommand (UUID plantId) {}