package pe.iotteam.plantcare.plant.infrastructure.persistence.jpa.mappers;

import pe.iotteam.plantcare.plant.domain.model.aggregates.WateringLog;
import pe.iotteam.plantcare.plant.domain.model.entities.PlantEntity;
import pe.iotteam.plantcare.plant.domain.model.entities.WateringLogEntity;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantId;

public class WateringLogMapper {

    /**
     * Maps a WateringLogEntity (JPA) to a WateringLog domain object, preserving the ID.
     */
    public static WateringLog toDomain(WateringLogEntity entity) {
        return new WateringLog(
                entity.getId(), // Preserve the ID
                new PlantId(entity.getPlant().getId()),
                entity.getWateredAt()
        );
    }

    /**
     * Maps a WateringLog domain object to a WateringLogEntity persistence object.
     */
    public static WateringLogEntity toEntity(WateringLog domain, PlantEntity plantEntity) {
        WateringLogEntity entity = new WateringLogEntity();
        if (domain.getId() != null) {
            entity.setId(domain.getId()); // Set existing ID if available
        }
        entity.setPlant(plantEntity);
        entity.setWateredAt(domain.getWateredAt());
        return entity;
    }
}