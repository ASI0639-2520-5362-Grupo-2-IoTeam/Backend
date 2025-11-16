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
        return Plant.builder()
                .id(entity.getId())
                .userId(new UserId(UUID.fromString(entity.getUserId())))
                .name(entity.getName())
                .type(entity.getType())
                .imgUrl(entity.getImgUrl())
                .bio(entity.getBio())
                .location(entity.getLocation())
                .status(PlantStatus.valueOf(entity.getStatus()))
                .lastWatered(entity.getLastWatered())
                .nextWatering(entity.getNextWatering())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .metrics(entity.getMetrics().stream().map(PlantMetricsMapper::toDomain).collect(Collectors.toList()))
                .wateringLogs(entity.getWateringLogs().stream().map(WateringLogMapper::toDomain).collect(Collectors.toList()))
                .build();
    }
}