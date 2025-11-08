package pe.iotteam.plantcare.plant.domain.model.aggregates;

import lombok.Getter;
import pe.iotteam.plantcare.plant.domain.model.commands.UpdatePlantCommand;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantId;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantStatus;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.UserId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Plant {

    private Long id;
    private final UserId userId;
    private String name;
    private String type;
    private String imgUrl;
    private String bio;
    private String location;
    private PlantStatus status;
    private final List<PlantMetrics> metrics;
    private final List<WateringLog> wateringLogs;
    private LocalDateTime lastWatered;
    private LocalDateTime nextWatering;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Plant(UserId userId, String name, String type, String imgUrl, String bio, String location) {
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.imgUrl = imgUrl;
        this.bio = bio;
        this.location = location;
        this.status = PlantStatus.HEALTHY;
        this.metrics = new ArrayList<>();
        this.wateringLogs = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Plant(Long id, UserId userId, String name, String type, String imgUrl,
                 String bio, String location, PlantStatus status, LocalDateTime lastWatered, LocalDateTime nextWatering,
                 LocalDateTime createdAt, LocalDateTime updatedAt,
                 List<PlantMetrics> metrics, List<WateringLog> wateringLogs) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.imgUrl = imgUrl;
        this.bio = bio;
        this.location = location;
        this.status = status != null ? status : PlantStatus.HEALTHY;
        this.lastWatered = lastWatered;
        this.nextWatering = nextWatering;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.updatedAt = updatedAt != null ? updatedAt : LocalDateTime.now();
        this.metrics = metrics != null ? metrics : new ArrayList<>();
        this.wateringLogs = wateringLogs != null ? wateringLogs : new ArrayList<>();
    }

    public void update(UpdatePlantCommand command) {
        this.name = command.name();
        this.type = command.type();
        this.imgUrl = command.imgUrl();
        this.bio = command.bio();
        this.location = command.location();
        this.updatedAt = LocalDateTime.now();
    }

    public void addMetric(PlantMetrics metric) {
        this.metrics.add(metric);
    }

    public void determineStatusFrom(PlantMetrics latestMetrics) {
        if (isCritical(latestMetrics)) {
            this.status = PlantStatus.CRITICAL;
        } else if (isWarning(latestMetrics)) {
            this.status = PlantStatus.WARNING;
        } else {
            this.status = PlantStatus.HEALTHY;
        }
    }

    /**
     * Records a watering event and calculates the next watering time.
     * @param latestMetrics The current sensor data, used to calculate the next watering time.
     */
    public void water(PlantMetrics latestMetrics) {
        this.lastWatered = LocalDateTime.now();
        this.wateringLogs.add(new WateringLog(new PlantId(this.id)));
        this.nextWatering = calculateNextWatering(latestMetrics);
        this.updatedAt = LocalDateTime.now();
    }

    private LocalDateTime calculateNextWatering(PlantMetrics metrics) {
        // Base watering interval: 3 days
        long baseHours = 72;

        // Adjust based on temperature (higher temp = more frequent watering)
        if (metrics.getTemperature() > 25) {
            baseHours -= 12; // Subtract 12 hours
        } else if (metrics.getTemperature() < 18) {
            baseHours += 12; // Add 12 hours
        }

        // Adjust based on humidity (lower humidity = more frequent watering)
        if (metrics.getHumidity() < 40) {
            baseHours -= 8; // Subtract 8 hours
        } else if (metrics.getHumidity() > 60) {
            baseHours += 8; // Add 8 hours
        }

        return this.lastWatered.plusHours(baseHours);
    }

    private boolean isCritical(PlantMetrics metrics) {
        return metrics.getTemperature() < 10 || metrics.getTemperature() > 35 ||
               metrics.getHumidity() < 30 || metrics.getHumidity() > 70 ||
               metrics.getSoilHumidity() < 20 || metrics.getSoilHumidity() > 80;
    }

    private boolean isWarning(PlantMetrics metrics) {
        return metrics.getTemperature() < 18 || metrics.getTemperature() > 26 ||
               metrics.getHumidity() < 40 || metrics.getHumidity() > 60 ||
               metrics.getSoilHumidity() < 30 || metrics.getSoilHumidity() > 70;
    }
}