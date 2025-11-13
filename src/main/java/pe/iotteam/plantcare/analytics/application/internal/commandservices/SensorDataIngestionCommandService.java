package pe.iotteam.plantcare.analytics.application.internal.commandservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.iotteam.plantcare.analytics.domain.exceptions.DataIngestionException;
import pe.iotteam.plantcare.analytics.domain.model.aggregates.SensorDataRecord;
import pe.iotteam.plantcare.analytics.domain.model.commands.IngestSensorDataCommand;
import pe.iotteam.plantcare.analytics.infrastructure.client.EdgeServiceClient;
import pe.iotteam.plantcare.analytics.infrastructure.client.ExternalSensorDataDto;
import pe.iotteam.plantcare.analytics.infrastructure.persistence.jpa.repositories.SensorDataRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Command service for ingesting sensor data
 * Implements incremental ingestion logic to avoid duplicates
 */
@Service
@Transactional
public class SensorDataIngestionCommandService {

    private static final Logger log = LoggerFactory.getLogger(SensorDataIngestionCommandService.class);

    private final SensorDataRepository repository;
    private final EdgeServiceClient edgeServiceClient;

    public SensorDataIngestionCommandService(SensorDataRepository repository, 
                                            EdgeServiceClient edgeServiceClient) {
        this.repository = repository;
        this.edgeServiceClient = edgeServiceClient;
    }

    /**
     * Handle data ingestion command with incremental logic
     * Only ingests records that are newer than the latest record in the database
     * 
     * @param command The ingestion command
     * @return Number of new records ingested
     */
    public int handle(IngestSensorDataCommand command) {
        log.info("Starting incremental data ingestion process");
        
        try {
            // 1. Fetch all data from external API
            List<ExternalSensorDataDto> externalData = edgeServiceClient.fetchAllSensorData();
            
            if (externalData == null || externalData.isEmpty()) {
                log.info("No data received from external API");
                return 0;
            }
            
            log.info("Received {} records from external API", externalData.size());
            
            // 2. Get the latest record timestamp from our database
            LocalDateTime latestTimestamp = repository.findLatestRecord()
                    .map(SensorDataRecord::getCreatedAt)
                    .orElse(null);
            
            if (latestTimestamp != null) {
                log.info("Latest record in database: {}", latestTimestamp);
            } else {
                log.info("No existing records in database, will ingest all data");
            }
            
            // 3. Filter only new records (incremental ingestion)
            List<SensorDataRecord> newRecords = new ArrayList<>();
            int duplicatesSkipped = 0;
            
            for (ExternalSensorDataDto dto : externalData) {
                // Skip records that are older than or equal to our latest record
                if (latestTimestamp != null && !dto.getCreatedAt().isAfter(latestTimestamp)) {
                    duplicatesSkipped++;
                    continue;
                }
                
                // Double-check: verify record doesn't exist in database
                if (repository.existsByDeviceIdAndCreatedAt(dto.getDeviceId(), dto.getCreatedAt())) {
                    log.debug("Skipping duplicate record: device_id={}, created_at={}", 
                             dto.getDeviceId(), dto.getCreatedAt());
                    duplicatesSkipped++;
                    continue;
                }
                
                // Convert DTO to domain model
                SensorDataRecord record = new SensorDataRecord(
                        dto.getDeviceId(),
                        dto.getTemperature(),
                        dto.getHumidity(),
                        dto.getLight(),
                        dto.getSoilHumidity(),
                        dto.getCreatedAt()
                );
                
                newRecords.add(record);
            }
            
            // 4. Save only new records
            if (!newRecords.isEmpty()) {
                repository.saveAll(newRecords);
                log.info("Successfully ingested {} new records", newRecords.size());
            } else {
                log.info("No new records to ingest");
            }
            
            log.info("Ingestion summary: {} new records, {} duplicates skipped", 
                     newRecords.size(), duplicatesSkipped);
            
            return newRecords.size();
            
        } catch (DataIngestionException e) {
            log.error("Data ingestion failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during data ingestion", e);
            throw new DataIngestionException("Unexpected error during data ingestion", e);
        }
    }
}
