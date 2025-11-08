package pe.iotteam.plantcare.plant.domain.model.aggregates;

import lombok.Getter;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantId;

import java.time.LocalDateTime;

@Getter
public class PlantMetrics {
    private Long id;
    private PlantId plantId;
    private String deviceId;
    private Integer temperature;
    private Integer humidity;
    private Integer light;
    private Integer soilHumidity;
    private LocalDateTime createdAt;

    // Constructor for live data
    public PlantMetrics(PlantId plantId, String deviceId, Integer temperature, Integer humidity, Integer light, Integer soilHumidity) {
        this.plantId = plantId;
        this.deviceId = deviceId;
        this.temperature = temperature;
        this.humidity = humidity;
        this.light = light;
        this.soilHumidity = soilHumidity;
        this.createdAt = LocalDateTime.now();
    }

    // Constructor for persisted data
    public PlantMetrics(Long id, PlantId plantId, String deviceId, Integer temperature, Integer humidity, Integer light, Integer soilHumidity, LocalDateTime createdAt) {
        this.id = id;
        this.plantId = plantId;
        this.deviceId = deviceId;
        this.temperature = temperature;
        this.humidity = humidity;
        this.light = light;
        this.soilHumidity = soilHumidity;
        this.createdAt = createdAt;
    }
}