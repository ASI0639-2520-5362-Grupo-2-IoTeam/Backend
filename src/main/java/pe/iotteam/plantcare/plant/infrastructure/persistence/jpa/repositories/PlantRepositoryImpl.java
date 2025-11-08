package pe.iotteam.plantcare.plant.infrastructure.persistence.jpa.repositories;

import org.springframework.stereotype.Repository;
import pe.iotteam.plantcare.plant.domain.model.aggregates.Plant;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantId;
import pe.iotteam.plantcare.plant.infrastructure.persistence.jpa.mappers.PlantMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class PlantRepositoryImpl implements PlantRepository {

    private final PlantJpaRepository jpaRepository;

    public PlantRepositoryImpl(PlantJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Plant> findById(PlantId id) {
        return jpaRepository.findById(id.value())
                .map(PlantMapper::toDomain);
    }

    @Override
    public List<Plant> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId.toString())
                .stream()
                .map(PlantMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Plant save(Plant plant) {
        var entity = PlantMapper.toEntity(plant);
        var saved = jpaRepository.save(entity);
        return PlantMapper.toDomain(saved);
    }

    @Override
    public void delete(PlantId id) {
        jpaRepository.deleteById(id.value());
    }

}