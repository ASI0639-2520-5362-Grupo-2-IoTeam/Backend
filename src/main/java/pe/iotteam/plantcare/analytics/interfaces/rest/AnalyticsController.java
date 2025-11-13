package pe.iotteam.plantcare.analytics.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.iotteam.plantcare.analytics.application.internal.commandservices.SensorDataIngestionCommandService;
import pe.iotteam.plantcare.analytics.application.internal.queryservices.SensorDataQueryService;
import pe.iotteam.plantcare.analytics.domain.exceptions.DataIngestionException;
import pe.iotteam.plantcare.analytics.domain.model.aggregates.SensorDataRecord;
import pe.iotteam.plantcare.analytics.domain.model.commands.IngestSensorDataCommand;
import pe.iotteam.plantcare.analytics.domain.model.queries.GetAllSensorDataQuery;
import pe.iotteam.plantcare.analytics.domain.model.queries.GetSensorDataByDateRangeQuery;
import pe.iotteam.plantcare.analytics.domain.model.queries.GetSensorDataByDeviceIdQuery;
import pe.iotteam.plantcare.analytics.interfaces.rest.resources.IngestionResultResource;
import pe.iotteam.plantcare.analytics.interfaces.rest.resources.SensorDataResource;
import pe.iotteam.plantcare.analytics.interfaces.rest.transform.SensorDataResourceAssembler;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for Analytics Bounded Context
 * Provides endpoints for sensor data ingestion and queries
 */
@RestController
@RequestMapping("/api/v1/analytics")
@Tag(name = "Analytics", description = "Analytics API for sensor data ingestion and querying")
public class AnalyticsController {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsController.class);

    private final SensorDataIngestionCommandService ingestionService;
    private final SensorDataQueryService queryService;

    public AnalyticsController(SensorDataIngestionCommandService ingestionService,
                              SensorDataQueryService queryService) {
        this.ingestionService = ingestionService;
        this.queryService = queryService;
    }

    /**
     * Trigger manual data ingestion from external API
     * POST /api/v1/analytics/ingest
     */
    @PostMapping("/ingest")
    @Operation(
            summary = "Trigger data ingestion",
            description = "Manually trigger ingestion of sensor data from external Edge Service API. Only new records will be ingested (incremental)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingestion completed successfully",
                    content = @Content(schema = @Schema(implementation = IngestionResultResource.class))),
            @ApiResponse(responseCode = "500", description = "Ingestion failed",
                    content = @Content(schema = @Schema(implementation = IngestionResultResource.class)))
    })
    public ResponseEntity<IngestionResultResource> ingestData() {
        log.info("Manual data ingestion triggered via API");
        
        try {
            int recordsIngested = ingestionService.handle(new IngestSensorDataCommand());
            return ResponseEntity.ok(IngestionResultResource.success(recordsIngested));
            
        } catch (DataIngestionException e) {
            log.error("Data ingestion failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(IngestionResultResource.failure(e.getMessage()));
        }
    }

    /**
     * Get all sensor data records
     * GET /api/v1/analytics/sensor-data
     */
    @GetMapping("/sensor-data")
    @Operation(
            summary = "Get all sensor data",
            description = "Retrieve all sensor data records from the analytics database, ordered by creation date (most recent first)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sensor data")
    })
    public ResponseEntity<List<SensorDataResource>> getAllSensorData() {
        log.debug("Retrieving all sensor data");
        
        List<SensorDataRecord> records = queryService.handle(new GetAllSensorDataQuery());
        List<SensorDataResource> resources = SensorDataResourceAssembler.toResourceList(records);
        
        return ResponseEntity.ok(resources);
    }

    /**
     * Get sensor data by device ID
     * GET /api/v1/analytics/sensor-data/device/{deviceId}
     */
    @GetMapping("/sensor-data/device/{deviceId}")
    @Operation(
            summary = "Get sensor data by device ID",
            description = "Retrieve all sensor data records for a specific device."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sensor data"),
            @ApiResponse(responseCode = "404", description = "No data found for the specified device")
    })
    public ResponseEntity<List<SensorDataResource>> getSensorDataByDevice(
            @Parameter(description = "Device ID", example = "device_001")
            @PathVariable String deviceId) {
        log.debug("Retrieving sensor data for device: {}", deviceId);
        
        List<SensorDataRecord> records = queryService.handle(new GetSensorDataByDeviceIdQuery(deviceId));
        List<SensorDataResource> resources = SensorDataResourceAssembler.toResourceList(records);
        
        return ResponseEntity.ok(resources);
    }

    /**
     * Get sensor data by date range
     * GET /api/v1/analytics/sensor-data/range
     */
    @GetMapping("/sensor-data/range")
    @Operation(
            summary = "Get sensor data by date range",
            description = "Retrieve sensor data records within a specified date range."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sensor data"),
            @ApiResponse(responseCode = "400", description = "Invalid date range parameters")
    })
    public ResponseEntity<List<SensorDataResource>> getSensorDataByDateRange(
            @Parameter(description = "Start date (ISO format)", example = "2025-11-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            
            @Parameter(description = "End date (ISO format)", example = "2025-11-13T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        log.debug("Retrieving sensor data between {} and {}", startDate, endDate);
        
        try {
            List<SensorDataRecord> records = queryService.handle(
                    new GetSensorDataByDateRangeQuery(startDate, endDate)
            );
            
            List<SensorDataResource> resources = SensorDataResourceAssembler.toResourceList(records);
            return ResponseEntity.ok(resources);
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid date range: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
