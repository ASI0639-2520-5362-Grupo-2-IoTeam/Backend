package pe.iotteam.plantcare.analytics.domain.model.aggregates;

import lombok.Getter;
import pe.iotteam.plantcare.analytics.domain.model.valueobjects.SensorDataId;

import java.time.LocalDateTime;

/**
 * SensorDataRecord aggregate root
 * Represents a sensor data record from the external Edge Service API
 */
@Getter
public class SensorDataRecord {
    private SensorDataId id;
    private String deviceId;
    private Integer temperature;
    private Integer humidity;
    private Integer light;
    private Integer soilHumidity;
    private LocalDateTime createdAt;

    // Constructor for new records from API
    public SensorDataRecord(String deviceId, Integer temperature, Integer humidity, 
                           Integer light, Integer soilHumidity, LocalDateTime createdAt) {
        this.deviceId = deviceId;
        this.temperature = temperature;
        this.humidity = humidity;
        this.light = light;
        this.soilHumidity = soilHumidity;
        this.createdAt = createdAt;
    }

    // Constructor for persisted records
    public SensorDataRecord(SensorDataId id, String deviceId, Integer temperature, 
                           Integer humidity, Integer light, Integer soilHumidity, 
                           LocalDateTime createdAt) {
        this.id = id;
        this.deviceId = deviceId;
        this.temperature = temperature;
        this.humidity = humidity;
        this.light = light;
        this.soilHumidity = soilHumidity;
        this.createdAt = createdAt;
    }

    /**
     * Creates a unique identifier for this record based on device_id and timestamp
     * This is used to detect duplicates during ingestion
     */
    public String getUniqueIdentifier() {
        return deviceId + "_" + createdAt.toString();
    }
}
