package pe.iotteam.plantcare.plant.application.internal.commandservices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.iotteam.plantcare.plant.domain.model.aggregates.Plant;
import pe.iotteam.plantcare.plant.domain.model.aggregates.PlantMetrics;
import pe.iotteam.plantcare.plant.domain.model.commands.CreatePlantCommand;
import pe.iotteam.plantcare.plant.domain.model.commands.DeletePlantCommand;
import pe.iotteam.plantcare.plant.domain.model.commands.UpdatePlantCommand;
import pe.iotteam.plantcare.plant.domain.model.commands.WaterPlantCommand;
import pe.iotteam.plantcare.plant.domain.model.exceptions.PlantNotFoundException;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantId;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.UserId;
import pe.iotteam.plantcare.plant.infrastructure.clients.EdgeServiceClient;
import pe.iotteam.plantcare.plant.infrastructure.persistence.jpa.repositories.PlantRepository;
import pe.iotteam.plantcare.plant.interfaces.rest.resources.SensorData;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlantCommandService {

    private final PlantRepository plantRepository;
    private final EdgeServiceClient edgeServiceClient;

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
        Plant plant = findPlantById(command.plantId());
        plant.update(command);
        return plantRepository.save(plant);
    }

    public void handle(DeletePlantCommand command) {
        if (!plantRepository.existsById(new PlantId(command.plantId()))) {
            throw new PlantNotFoundException(command.plantId());
        }
        plantRepository.deleteById(new PlantId(command.plantId()));
    }

    public Plant handle(WaterPlantCommand command) {
        Plant plant = findPlantById(command.plantId());

        Optional<SensorData> sensorDataOpt = edgeServiceClient.getLatestSensorData();
        PlantMetrics liveMetric = sensorDataOpt.map(sensorData -> PlantMetrics.builder()
                .plantId(new PlantId(plant.getId()))
                .deviceId(sensorData.getDeviceId())
                .temperature(sensorData.getAirTemperatureCelsius())
                .humidity(sensorData.getAirHumidityPercent() != null ? sensorData.getAirHumidityPercent().intValue() : 0)
                .light(sensorData.getLuminosityLux())
                .soilHumidity(sensorData.getSoilMoisturePercent())
                .build()
        ).orElse(PlantMetrics.builder()
                .plantId(new PlantId(plant.getId()))
                .temperature(0)
                .humidity(0)
                .light(0)
                .soilHumidity(0)
                .build()); // Default metrics

        plant.water(liveMetric);
        return plantRepository.save(plant);
    }

    private Plant findPlantById(Long plantId) {
        return plantRepository.findById(new PlantId(plantId))
                .orElseThrow(() -> new PlantNotFoundException(plantId));
    }
}
