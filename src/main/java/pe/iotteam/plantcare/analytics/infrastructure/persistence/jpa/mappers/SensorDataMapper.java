package pe.iotteam.plantcare.analytics.infrastructure.persistence.jpa.mappers;

import pe.iotteam.plantcare.analytics.domain.model.aggregates.SensorDataRecord;
import pe.iotteam.plantcare.analytics.domain.model.entities.SensorDataEntity;
import pe.iotteam.plantcare.analytics.domain.model.valueobjects.SensorDataId;

/**
 * Mapper between SensorDataEntity (JPA) and SensorDataRecord (Domain)
 */
public class SensorDataMapper {

    /**
     * Convert JPA entity to domain aggregate
     */
    public static SensorDataRecord toDomain(SensorDataEntity entity) {
        return new SensorDataRecord(
                new SensorDataId(entity.getId()),
                entity.getDeviceId(),
                entity.getTemperature(),
                entity.getHumidity(),
                entity.getLight(),
                entity.getSoilHumidity(),
                entity.getCreatedAt()
        );
    }

    /**
     * Convert domain aggregate to JPA entity
     */
    public static SensorDataEntity toEntity(SensorDataRecord domain) {
        return new SensorDataEntity(
                domain.getDeviceId(),
                domain.getTemperature(),
                domain.getHumidity(),
                domain.getLight(),
                domain.getSoilHumidity(),
                domain.getCreatedAt()
        );
    }
}
