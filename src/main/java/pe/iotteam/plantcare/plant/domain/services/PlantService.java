package pe.iotteam.plantcare.plant.domain.services;

import pe.iotteam.plantcare.plant.domain.model.aggregates.Plant;
import pe.iotteam.plantcare.plant.domain.model.commands.CreatePlantCommand;
import pe.iotteam.plantcare.plant.domain.model.commands.DeletePlantCommand;
import pe.iotteam.plantcare.plant.domain.model.commands.UpdatePlantCommand;
import pe.iotteam.plantcare.plant.domain.model.queries.GetPlantByIdQuery;

import java.util.Optional;

public interface PlantService {
    Optional<Plant> handle(GetPlantByIdQuery query);
    Plant handle(CreatePlantCommand command);
    Plant handle(UpdatePlantCommand command);
    void handle(DeletePlantCommand command);
}