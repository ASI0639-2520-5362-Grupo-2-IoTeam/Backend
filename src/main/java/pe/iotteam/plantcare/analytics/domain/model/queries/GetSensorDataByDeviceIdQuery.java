package pe.iotteam.plantcare.analytics.domain.model.queries;

/**
 * Query to retrieve sensor data by device ID
 */
public record GetSensorDataByDeviceIdQuery(String deviceId) {
    public GetSensorDataByDeviceIdQuery {
        if (deviceId == null || deviceId.isBlank()) {
            throw new IllegalArgumentException("Device ID cannot be null or empty");
        }
    }
}
