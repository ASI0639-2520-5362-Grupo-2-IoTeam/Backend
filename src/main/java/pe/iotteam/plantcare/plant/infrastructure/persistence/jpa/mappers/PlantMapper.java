package pe.iotteam.plantcare.plant.infrastructure.persistence.jpa.mappers;

import pe.iotteam.plantcare.plant.domain.model.aggregates.Plant;
import pe.iotteam.plantcare.plant.domain.model.entities.PlantEntity;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantStatus;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.UserId;

import java.util.UUID;
import java.util.stream.Collectors;

public class PlantMapper {

    /**
     * Updates an existing PlantEntity with data from a Plant domain object.
     * This method is crucial for correctly handling updates to collections.
     */
    public static void updateEntityFromDomain(PlantEntity entity, Plant plant) {
        entity.setUserId(plant.getUserId().value().toString());
        entity.setName(plant.getName());
        entity.setType(plant.getType());
        entity.setImgUrl(plant.getImgUrl());
        entity.setBio(plant.getBio());
        entity.setLocation(plant.getLocation());
        entity.setStatus(plant.getStatus().name());
        entity.setLastWatered(plant.getLastWatered());
        entity.setNextWatering(plant.getNextWatering());
        entity.setUpdatedAt(plant.getUpdatedAt());

        // Correctly synchronize the watering logs collection
        var newLogs = plant.getWateringLogs().stream()
                .filter(log -> log.getId() == null) // Filter for new logs only
                .map(log -> WateringLogMapper.toEntity(log, entity))
                .collect(Collectors.toList());

        entity.getWateringLogs().addAll(newLogs);
    }

    public static Plant toDomain(PlantEntity entity) {
        return new Plant(
                entity.getId(),
                new UserId(UUID.fromString(entity.getUserId())),
                entity.getName(),
                entity.getType(),
                entity.getImgUrl(),
                entity.getBio(),
                entity.getLocation(),
                PlantStatus.valueOf(entity.getStatus()),
                entity.getLastWatered(),
                entity.getNextWatering(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getMetrics().stream().map(PlantMetricsMapper::toDomain).collect(Collectors.toList()),
                entity.getWateringLogs().stream().map(WateringLogMapper::toDomain).collect(Collectors.toList())
        );
    }
}