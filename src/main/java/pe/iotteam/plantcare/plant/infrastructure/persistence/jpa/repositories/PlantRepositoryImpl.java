package pe.iotteam.plantcare.plant.infrastructure.persistence.jpa.repositories;

import org.springframework.stereotype.Repository;
import pe.iotteam.plantcare.plant.domain.model.aggregates.Plant;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantId;
import pe.iotteam.plantcare.plant.infrastructure.persistence.jpa.mappers.PlantMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PlantRepositoryImpl implements PlantRepository {

    private final PlantJpaRepository jpaRepository;

    public PlantRepositoryImpl(PlantJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Plant> findById(PlantId id) {
        return jpaRepository.findById(id.value())
                .map(PlantMapper::toAggregate);
    }

    @Override
    public List<Plant> findByUserId(UUID userId) {
        return jpaRepository.findByUserId_Value(userId)
                .stream()
                .map(PlantMapper::toAggregate)
                .toList();
    }

    @Override
    public Plant save(Plant plant) {
        var entity = PlantMapper.toEntity(plant);
        var saved = jpaRepository.save(entity);
        return PlantMapper.toAggregate(saved);
    }

    @Override
    public void delete(PlantId id) {
        jpaRepository.deleteById(id.value());
    }
}