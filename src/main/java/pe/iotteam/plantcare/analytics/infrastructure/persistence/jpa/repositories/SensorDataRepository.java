package pe.iotteam.plantcare.analytics.infrastructure.persistence.jpa.repositories;

import pe.iotteam.plantcare.analytics.domain.model.aggregates.SensorDataRecord;
import pe.iotteam.plantcare.analytics.domain.model.valueobjects.SensorDataId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Domain Repository interface for SensorDataRecord
 */
public interface SensorDataRepository {
    
    /**
     * Save a sensor data record
     */
    SensorDataRecord save(SensorDataRecord record);
    
    /**
     * Save multiple sensor data records
     */
    List<SensorDataRecord> saveAll(List<SensorDataRecord> records);
    
    /**
     * Find all sensor data records
     */
    List<SensorDataRecord> findAll();
    
    /**
     * Find sensor data by device ID
     */
    List<SensorDataRecord> findByDeviceId(String deviceId);
    
    /**
     * Find the latest record by timestamp
     */
    Optional<SensorDataRecord> findLatestRecord();
    
    /**
     * Check if a record exists by device ID and timestamp
     */
    boolean existsByDeviceIdAndCreatedAt(String deviceId, LocalDateTime createdAt);
}
