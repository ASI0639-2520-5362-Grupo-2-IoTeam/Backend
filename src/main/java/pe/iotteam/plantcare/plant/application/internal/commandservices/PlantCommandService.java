package pe.iotteam.plantcare.plant.application.internal.commandservices;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import pe.iotteam.plantcare.plant.domain.model.aggregates.Plant;
import pe.iotteam.plantcare.plant.domain.model.aggregates.PlantMetrics;
import pe.iotteam.plantcare.plant.domain.model.commands.CreatePlantCommand;
import pe.iotteam.plantcare.plant.domain.model.commands.UpdatePlantCommand;
import pe.iotteam.plantcare.plant.domain.model.commands.DeletePlantCommand;
import pe.iotteam.plantcare.plant.domain.model.commands.WaterPlantCommand;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantId;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.UserId;
import pe.iotteam.plantcare.plant.infrastructure.persistence.jpa.repositories.PlantRepository;
import pe.iotteam.plantcare.plant.interfaces.rest.resources.SensorData;

@Service
@Transactional
public class PlantCommandService {

    private final PlantRepository plantRepository;
    private final RestTemplate restTemplate;

    @Value("${edge.service.base-url}")
    private String edgeServiceUrl;

    public PlantCommandService(PlantRepository plantRepository, RestTemplate restTemplate) {
        this.plantRepository = plantRepository;
        this.restTemplate = restTemplate;
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

    public Plant handle(WaterPlantCommand command) {
        var plant = plantRepository.findById(new PlantId(command.plantId())).orElseThrow(() -> new RuntimeException("Plant not found"));

        // Get live sensor data to calculate next watering
        var response = restTemplate.getForEntity(edgeServiceUrl, SensorData[].class);
        SensorData latestSensorData = response.getBody() != null && response.getBody().length > 0 ? response.getBody()[0] : new SensorData();
        var liveMetric = new PlantMetrics(
            new PlantId(plant.getId()),
            latestSensorData.getDeviceId(),
            latestSensorData.getTemperature(),
            latestSensorData.getHumidity() != null ? latestSensorData.getHumidity().intValue() : 0,
            latestSensorData.getLight(),
            latestSensorData.getSoilHumidity()
        );

        plant.water(liveMetric);
        return plantRepository.save(plant);
    }
}