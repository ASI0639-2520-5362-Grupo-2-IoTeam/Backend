package pe.iotteam.plantcare.plant.infrastructure.persistence.jpa.mappers;

import pe.iotteam.plantcare.plant.domain.model.aggregates.PlantMetrics;
import pe.iotteam.plantcare.plant.domain.model.entities.PlantMetricsEntity;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantId;

public class PlantMetricsMapper {
    public static PlantMetrics toDomain(PlantMetricsEntity entity) {
        return new PlantMetrics(
                entity.getId(),
                new PlantId(entity.getPlant().getId()),
                entity.getDeviceId(),
                entity.getTemperature(),
                entity.getHumidity(),
                entity.getLight(),
                entity.getSoilHumidity(),
                entity.getCreatedAt()
        );
    }

    public static PlantMetricsEntity toEntity(PlantMetrics domain) {
        PlantMetricsEntity entity = new PlantMetricsEntity();
        // The plant entity will be set in the service
        entity.setDeviceId(domain.getDeviceId());
        entity.setTemperature(domain.getTemperature());
        entity.setHumidity(domain.getHumidity());
        entity.setLight(domain.getLight());
        entity.setSoilHumidity(domain.getSoilHumidity());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }
}