package pe.iotteam.plantcare.analytics.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.iotteam.plantcare.analytics.domain.model.entities.SensorDataEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for SensorDataEntity
 */
@Repository
public interface SensorDataJpaRepository extends JpaRepository<SensorDataEntity, Long> {
    
    /**
     * Find sensor data by device ID
     */
    List<SensorDataEntity> findByDeviceId(String deviceId);
    
    /**
     * Find the latest record by timestamp
     * Used to implement incremental ingestion
     */
    @Query("SELECT s FROM SensorDataEntity s ORDER BY s.createdAt DESC LIMIT 1")
    Optional<SensorDataEntity> findLatestRecord();
    
    /**
     * Check if a record exists by device ID and timestamp
     * Used to prevent duplicates
     */
    boolean existsByDeviceIdAndCreatedAt(String deviceId, LocalDateTime createdAt);
    
    /**
     * Find all records ordered by creation date (most recent first)
     */
    List<SensorDataEntity> findAllByOrderByCreatedAtDesc();
}
