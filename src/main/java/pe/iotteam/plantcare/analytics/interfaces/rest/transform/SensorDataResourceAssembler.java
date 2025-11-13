package pe.iotteam.plantcare.analytics.interfaces.rest.transform;

import pe.iotteam.plantcare.analytics.domain.model.aggregates.SensorDataRecord;
import pe.iotteam.plantcare.analytics.interfaces.rest.resources.SensorDataResource;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Assembler to transform between domain models and REST resources
 */
public class SensorDataResourceAssembler {

    /**
     * Transform domain model to REST resource
     */
    public static SensorDataResource toResource(SensorDataRecord record) {
        return new SensorDataResource(
                record.getId() != null ? record.getId().value() : null,
                record.getDeviceId(),
                record.getTemperature(),
                record.getHumidity(),
                record.getLight(),
                record.getSoilHumidity(),
                record.getCreatedAt()
        );
    }

    /**
     * Transform list of domain models to list of REST resources
     */
    public static List<SensorDataResource> toResourceList(List<SensorDataRecord> records) {
        return records.stream()
                .map(SensorDataResourceAssembler::toResource)
                .collect(Collectors.toList());
    }
}
