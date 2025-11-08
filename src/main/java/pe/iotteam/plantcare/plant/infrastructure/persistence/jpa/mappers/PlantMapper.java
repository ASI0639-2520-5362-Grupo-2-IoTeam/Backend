package pe.iotteam.plantcare.plant.infrastructure.persistence.jpa.mappers;

import pe.iotteam.plantcare.plant.domain.model.aggregates.Plant;
import pe.iotteam.plantcare.plant.domain.model.entities.PlantEntity;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.UserId;

import java.util.UUID;

public class PlantMapper {

    public static PlantEntity toEntity(Plant plant) {
        PlantEntity entity = new PlantEntity();
        entity.setId(plant.getId());
        entity.setUserId(plant.getUserId().value().toString());
        entity.setName(plant.getName());
        entity.setType(plant.getType());
        entity.setImgUrl(plant.getImgUrl());
        entity.setBio(plant.getBio());
        entity.setLocation(plant.getLocation());
        entity.setStatus(plant.getStatus().name());
        entity.setCreatedAt(plant.getCreatedAt());
        entity.setUpdatedAt(plant.getUpdatedAt());
        if (plant.getLastWatered() != null)
            entity.setLastWatered(plant.getLastWatered());
        if (plant.getNextWatering() != null)
            entity.setNextWatering(plant.getNextWatering());
        return entity;
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
                pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantStatus.valueOf(entity.getStatus()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}