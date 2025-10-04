package pe.iotteam.plantcare.plant.infrastructure.persistence.jpa.mappers;

import pe.iotteam.plantcare.plant.domain.model.aggregates.Plant;
import pe.iotteam.plantcare.plant.domain.model.entities.PlantEntity;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantId;

public class PlantMapper {

    public static PlantEntity toEntity(Plant plant) {
        PlantEntity entity = new PlantEntity();
        entity.setId(plant.getPlantId().value());
        entity.setUserId(plant.getUserId());
        entity.setName(plant.getName());
        entity.setType(plant.getType());
        entity.setImgUrl(plant.getImgUrl());
        entity.setBio(plant.getBio());
        entity.setLocation(plant.getLocation());
        entity.setStatus(plant.getStatus());
        entity.setCreatedAt(plant.getCreatedAt());
        entity.setUpdatedAt(plant.getUpdatedAt());
        if (plant.getLastWatered() != null)
            entity.setLastWatered(plant.getLastWatered().toString());
        if (plant.getNextWatering() != null)
            entity.setNextWatering(plant.getNextWatering().toString());
        return entity;
    }

    public static Plant toAggregate(PlantEntity entity) {
        Plant aggregate = new Plant(
                new PlantId(entity.getId()),
                entity.getUserId(),
                entity.getName(),
                entity.getType(),
                entity.getImgUrl(),
                entity.getBio(),
                entity.getLocation()
        );
        aggregate.water();
        return aggregate;
    }
}