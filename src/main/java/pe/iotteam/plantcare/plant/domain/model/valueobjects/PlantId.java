package pe.iotteam.plantcare.plant.domain.model.valueobjects;

public record PlantId(Long value) {
    public PlantId {
        if (value == null || value <= 0) throw new IllegalArgumentException("Invalid PlantId");
    }
}