package pe.iotteam.plantcare.analytics.application.internal.queryservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.iotteam.plantcare.analytics.domain.model.aggregates.SensorDataRecord;
import pe.iotteam.plantcare.analytics.domain.model.queries.GetAllSensorDataQuery;
import pe.iotteam.plantcare.analytics.domain.model.queries.GetSensorDataByDeviceIdQuery;
import pe.iotteam.plantcare.analytics.infrastructure.persistence.jpa.repositories.SensorDataRepository;

import java.util.List;

/**
 * Query service for retrieving sensor data
 */
@Service
@Transactional(readOnly = true)
public class SensorDataQueryService {

    private static final Logger log = LoggerFactory.getLogger(SensorDataQueryService.class);

    private final SensorDataRepository repository;

    public SensorDataQueryService(SensorDataRepository repository) {
        this.repository = repository;
    }

    /**
     * Handle query to get all sensor data
     */
    public List<SensorDataRecord> handle(GetAllSensorDataQuery query) {
        log.debug("Retrieving all sensor data records");
        List<SensorDataRecord> records = repository.findAll();
        log.debug("Found {} sensor data records", records.size());
        return records;
    }

    /**
     * Handle query to get sensor data by device ID
     */
    public List<SensorDataRecord> handle(GetSensorDataByDeviceIdQuery query) {
        log.debug("Retrieving sensor data for device: {}", query.deviceId());
        List<SensorDataRecord> records = repository.findByDeviceId(query.deviceId());
        log.debug("Found {} records for device {}", records.size(), query.deviceId());
        return records;
    }
}
