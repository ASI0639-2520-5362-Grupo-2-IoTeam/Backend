package pe.iotteam.plantcare.plant.domain.model.aggregates;

import lombok.Getter;
import pe.iotteam.plantcare.plant.domain.model.commands.UpdatePlantCommand;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantId;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantStatus;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.UserId;

import java.time.LocalDateTime;

@Getter
public class Plant {

    private final PlantId plantId;
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

    public Plant(PlantId plantId, UserId userId, String name, String type, String imgUrl, String bio, String location) {
        this.plantId = plantId;
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

    public void update(UpdatePlantCommand command) {
        this.name = command.name();
        this.type = command.type();
        this.imgUrl = command.imgUrl();
        this.bio = command.bio();
        this.location = command.location();
        this.updatedAt = LocalDateTime.now();
    }

    public void water() {
        this.lastWatered = LocalDateTime.now();
        this.nextWatering = this.lastWatered.plusDays(2); // Ejemplo de lógica
        this.updatedAt = LocalDateTime.now();
    }
}