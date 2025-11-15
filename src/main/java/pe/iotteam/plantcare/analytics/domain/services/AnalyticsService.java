package pe.iotteam.plantcare.analytics.domain.services;

import pe.iotteam.plantcare.analytics.domain.model.aggregates.SensorDataRecord;
import pe.iotteam.plantcare.analytics.domain.model.commands.IngestSensorDataCommand;
import pe.iotteam.plantcare.analytics.domain.model.queries.GetAllSensorDataQuery;
import pe.iotteam.plantcare.analytics.domain.model.queries.GetSensorDataByDeviceIdQuery;

import java.util.List;

/**
 * Domain service interface for Analytics bounded context
 */
public interface AnalyticsService {
    
    /**
     * Handle ingestion command
     */
    int handle(IngestSensorDataCommand command);
    
    /**
     * Handle query to get all sensor data
     */
    List<SensorDataRecord> handle(GetAllSensorDataQuery query);
    
    /**
     * Handle query to get sensor data by device ID
     */
    List<SensorDataRecord> handle(GetSensorDataByDeviceIdQuery query);
}
