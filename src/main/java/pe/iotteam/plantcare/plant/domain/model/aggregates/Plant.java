package pe.iotteam.plantcare.plant.domain.model.aggregates;

import lombok.Getter;
import pe.iotteam.plantcare.plant.domain.model.commands.UpdatePlantCommand;
import pe.iotteam.plantcare.plant.domain.model.commands.UpdatePlantMetricsCommand;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantStatus;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.UserId;

import java.time.LocalDateTime;

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
    private LocalDateTime lastWatered;
    private LocalDateTime nextWatering;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer temperature;
    private Double humidity;
    private Integer light;
    private Integer soil_humidity;

    /**
     * Constructor principal - el agregado genera su propio ID.
     */
    public Plant(UserId userId, String name, String type, String imgUrl, String bio, String location) {
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.imgUrl = imgUrl;
        this.bio = bio;
        this.location = location;
        this.status = PlantStatus.HEALTHY;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Constructor completo (usado al reconstruir desde la base de datos)
     */
    public Plant(Long  id, UserId userId, String name, String type, String imgUrl,
                 String bio, String location, PlantStatus status,
                 LocalDateTime createdAt, LocalDateTime updatedAt,
                 Integer temperature, Double humidity, Integer light, Integer soil_humidity) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.imgUrl = imgUrl;
        this.bio = bio;
        this.location = location;
        this.status = status != null ? status : PlantStatus.HEALTHY;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.updatedAt = updatedAt != null ? updatedAt : LocalDateTime.now();
        this.temperature = temperature;
        this.humidity = humidity;
        this.light = light;
        this.soil_humidity = soil_humidity;
    }

    public void update(UpdatePlantCommand command) {
        this.name = command.name();
        this.type = command.type();
        this.imgUrl = command.imgUrl();
        this.bio = command.bio();
        this.location = command.location();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateMetrics(UpdatePlantMetricsCommand command) {
        this.temperature = command.temperature();
        this.humidity = command.humidity();
        this.light = command.light();
        this.soil_humidity = command.soil_humidity();
        this.updatedAt = LocalDateTime.now();
    }
}