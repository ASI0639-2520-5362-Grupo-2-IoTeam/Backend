package pe.iotteam.plantcare.plant.infrastructure.persistence.jpa.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import pe.iotteam.plantcare.plant.domain.model.aggregates.Plant;
import pe.iotteam.plantcare.plant.domain.model.entities.PlantEntity;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantId;
import pe.iotteam.plantcare.plant.infrastructure.persistence.jpa.mappers.PlantMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class PlantRepositoryImpl implements PlantRepository {

    private static final Logger log = LoggerFactory.getLogger(PlantRepositoryImpl.class);

    private final PlantJpaRepository jpaRepository;

    public PlantRepositoryImpl(PlantJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Plant> findById(PlantId id) {
        return jpaRepository.findById(id.plantId())
                .map(PlantMapper::toDomain);
    }

    @Override
    public List<Plant> findByUserId(UUID userId) {
        // Logging to help debug why queries by userId return empty
        log.debug("PlantRepositoryImpl.findByUserId called with userId={}", userId);
        var entities = jpaRepository.findByUserId(userId.toString());
        log.debug("PlantJpaRepository.findByUserId returned {} entities for userId={}", entities.size(), userId);
        return entities.stream()
                .map(PlantMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Plant save(Plant plant) {
        // If the plant has an ID, it's an update. Otherwise, it's a new plant.
        PlantEntity entity = plant.getId() != null ?
                jpaRepository.findById(plant.getId()).orElse(new PlantEntity()) :
                new PlantEntity();

        // Map all data from the domain object to the entity
        PlantMapper.updateEntityFromDomain(entity, plant);

        var savedEntity = jpaRepository.save(entity);
        return PlantMapper.toDomain(savedEntity);
    }

    @Override
    public void delete(PlantId id) {
        jpaRepository.deleteById(id.plantId());
    }

}