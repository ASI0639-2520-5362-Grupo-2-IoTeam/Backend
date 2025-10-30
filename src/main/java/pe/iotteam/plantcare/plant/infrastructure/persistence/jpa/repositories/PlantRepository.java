package pe.iotteam.plantcare.plant.infrastructure.persistence.jpa.repositories;

import pe.iotteam.plantcare.plant.domain.model.aggregates.Plant;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlantRepository {
    Optional<Plant> findById(PlantId id);
    List<Plant> findByUserId(UUID userId);
    Plant save(Plant plant);
    void delete(PlantId id);
}