package pe.iotteam.plantcare.plant.domain.model.aggregates;

import lombok.Getter;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantId;

import java.time.LocalDateTime;

@Getter
public class WateringLog {
    private Long id;
    private final PlantId plantId;
    private final LocalDateTime wateredAt;

    /**
     * Constructor for new watering logs.
     */
    public WateringLog(PlantId plantId) {
        this.plantId = plantId;
        this.wateredAt = LocalDateTime.now();
    }

    /**
     * Constructor for existing logs being loaded from the database.
     */
    public WateringLog(Long id, PlantId plantId, LocalDateTime wateredAt) {
        this.id = id;
        this.plantId = plantId;
        this.wateredAt = wateredAt;
    }
}