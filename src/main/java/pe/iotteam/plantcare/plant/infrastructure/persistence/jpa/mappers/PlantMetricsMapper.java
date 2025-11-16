package pe.iotteam.plantcare.plant.infrastructure.persistence.jpa.mappers;

import pe.iotteam.plantcare.plant.domain.model.aggregates.PlantMetrics;
import pe.iotteam.plantcare.plant.domain.model.entities.PlantMetricsEntity;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantId;

public class PlantMetricsMapper {
    public static PlantMetrics toDomain(PlantMetricsEntity entity) {
        return PlantMetrics.builder()
                .id(entity.getId())
                .plantId(new PlantId(entity.getPlant().getId()))
                .deviceId(entity.getDeviceId())
                .temperature(entity.getTemperature())
                .humidity(entity.getHumidity())
                .light(entity.getLight())
                .soilHumidity(entity.getSoilHumidity())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static PlantMetricsEntity toEntity(PlantMetrics domain) {
        PlantMetricsEntity entity = new PlantMetricsEntity(
                domain.getDeviceId(),
                domain.getTemperature(),
                domain.getHumidity(),
                domain.getLight(),
                domain.getSoilHumidity()
        );
        // The plant entity will be set in the service
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }
}