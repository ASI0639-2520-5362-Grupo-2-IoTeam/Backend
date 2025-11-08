package pe.iotteam.plantcare.plant.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.iotteam.plantcare.plant.domain.model.aggregates.Plant;
import pe.iotteam.plantcare.plant.domain.model.commands.CreatePlantCommand;
import pe.iotteam.plantcare.plant.domain.model.commands.UpdatePlantCommand;
import pe.iotteam.plantcare.plant.domain.model.commands.DeletePlantCommand;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantId;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.UserId;
import pe.iotteam.plantcare.plant.infrastructure.persistence.jpa.repositories.PlantRepository;

@Service
@Transactional
public class PlantCommandService {

    private final PlantRepository plantRepository;

    public PlantCommandService(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    public Plant handle(CreatePlantCommand command) {
        Plant plant = new Plant(
                new UserId(command.userId()),
                command.name(),
                command.type(),
                command.imgUrl(),
                command.bio(),
                command.location()
        );
        return plantRepository.save(plant);
    }

    public Plant handle(UpdatePlantCommand command) {
        var plant = plantRepository.findById(new PlantId(command.plantId())).orElseThrow(() -> new RuntimeException("Plant not found"));
        plant.update(command);
        return plantRepository.save(plant);
    }

    public void handle(DeletePlantCommand command) {
        var plantId = new PlantId(command.plantId());
        if (plantRepository.findById(plantId).isEmpty()) {
            throw new RuntimeException("Plant not found");
        }
        plantRepository.delete(plantId);
    }
}