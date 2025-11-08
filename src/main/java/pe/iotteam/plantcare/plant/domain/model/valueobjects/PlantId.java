package pe.iotteam.plantcare.plant.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public record PlantId(Long plantId) implements Serializable {
    public PlantId() {
        this(0L);
    }
}