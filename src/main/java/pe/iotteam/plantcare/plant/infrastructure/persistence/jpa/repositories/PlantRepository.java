package pe.iotteam.plantcare.plant.infrastructure.persistence.jpa.repositories;

import pe.iotteam.plantcare.plant.domain.model.aggregates.Plant;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio de dominio para Plant. No extiende JpaRepository para evitar filtrar métodos innecesarios y mantener
 * una API enfocada (KISS/YAGNI). La persistencia concreta se maneja en {@link PlantRepositoryImpl} usando
 * {@link PlantJpaRepository} como adaptación.
 */
public interface PlantRepository {
    Optional<Plant> findById(PlantId id);
    List<Plant> findByUserId(UUID userId);
    Plant save(Plant plant);
    void delete(PlantId id);
    boolean existsById(PlantId id);
    void deleteById(PlantId id);
}
