package pe.iotteam.plantcare.analytics.application.internal.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pe.iotteam.plantcare.analytics.domain.model.commands.IngestSensorDataCommand;
import pe.iotteam.plantcare.analytics.application.internal.commandservices.SensorDataIngestionCommandService;

/**
 * Scheduled task for automatic incremental data ingestion
 * Runs periodically to fetch and store new sensor data from external API
 */
@Component
public class DataIngestionScheduler {

    private static final Logger log = LoggerFactory.getLogger(DataIngestionScheduler.class);

    private final SensorDataIngestionCommandService ingestionService;

    public DataIngestionScheduler(SensorDataIngestionCommandService ingestionService) {
        this.ingestionService = ingestionService;
    }

    /**
     * Scheduled task that runs every 5 minutes
     * Fetches new sensor data from external API and stores it incrementally
     * 
     * You can adjust the schedule by modifying the cron expression
     */
    @Scheduled(cron = "0 */5 * * * *")
    public void scheduledDataIngestion() {
        log.info("========== Starting scheduled data ingestion ==========");
        
        try {
            int newRecordsCount = ingestionService.handle(new IngestSensorDataCommand());
            
            if (newRecordsCount > 0) {
                log.info("Scheduled ingestion completed successfully: {} new records added", 
                         newRecordsCount);
            } else {
                log.info("Scheduled ingestion completed: No new records to add");
            }
            
        } catch (Exception e) {
            log.error("Scheduled data ingestion failed: {}", e.getMessage(), e);
        }
        
        log.info("========== Scheduled data ingestion finished ==========");
    }
    
    /**
     * Optional: Run ingestion on application startup
     * Uncomment the @Scheduled annotation to enable
     */
    // @Scheduled(initialDelay = 10000, fixedDelay = Long.MAX_VALUE) // Run once 10 seconds after startup
    public void initialDataIngestion() {
        log.info("Running initial data ingestion on startup");
        try {
            int newRecordsCount = ingestionService.handle(new IngestSensorDataCommand());
            log.info("Initial ingestion completed: {} records ingested", newRecordsCount);
        } catch (Exception e) {
            log.error("Initial data ingestion failed: {}", e.getMessage(), e);
        }
    }
}
