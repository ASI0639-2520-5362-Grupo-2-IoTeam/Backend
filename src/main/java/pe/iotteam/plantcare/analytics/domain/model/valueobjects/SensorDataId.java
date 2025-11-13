package pe.iotteam.plantcare.analytics.domain.model.valueobjects;

/**
 * Value Object for SensorDataRecord identifier
 */
public record SensorDataId(Long value) {
    public SensorDataId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("SensorDataId must be a positive number");
        }
    }
}
