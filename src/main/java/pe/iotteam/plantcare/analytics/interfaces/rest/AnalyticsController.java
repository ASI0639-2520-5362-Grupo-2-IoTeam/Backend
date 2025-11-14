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
 * Provides endpoints for sensor data import and queries
 */
@RestController
@RequestMapping("/api/v1/analytics")
@Tag(name = "Analytics", description = "Analytics API for sensor data import and querying")
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
     * Trigger manual data import from external API
     * POST /api/v1/analytics/imports
     */
    @PostMapping("/imports")
    @Operation(
            summary = "Import sensor data",
            description = "Import sensor data from external Edge Service API. Only new records will be imported (incremental import)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Import completed successfully",
                    content = @Content(schema = @Schema(implementation = IngestionResultResource.class))),
            @ApiResponse(responseCode = "500", description = "Import failed",
                    content = @Content(schema = @Schema(implementation = IngestionResultResource.class)))
    })
    public ResponseEntity<IngestionResultResource> importSensorData() {
        log.info("Manual data import triggered via API");
        
        try {
            int recordsIngested = ingestionService.handle(new IngestSensorDataCommand());
            return ResponseEntity.ok(IngestionResultResource.success(recordsIngested));
            
        } catch (DataIngestionException e) {
            log.error("Data import failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(IngestionResultResource.failure(e.getMessage()));
        }
    }

    /**
     * Get all sensor data records
     * GET /api/v1/analytics/data
     * 
     * Supports optional query parameters for filtering:
     * - start: Filter by start date
     * - end: Filter by end date
     */
    @GetMapping("/data")
    @Operation(
            summary = "Get sensor data",
            description = "Retrieve sensor data records from analytics database. Supports optional date range filtering via query parameters."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sensor data"),
            @ApiResponse(responseCode = "400", description = "Invalid query parameters")
    })
    public ResponseEntity<List<SensorDataResource>> getSensorData(
            @Parameter(description = "Start date for filtering (optional, ISO format)", example = "2025-11-01T00:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            
            @Parameter(description = "End date for filtering (optional, ISO format)", example = "2025-11-13T23:59:59")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        
        log.debug("Retrieving sensor data with filters - start: {}, end: {}", start, end);
        
        try {
            List<SensorDataRecord> records;
            
            // If both dates provided, filter by date range
            if (start != null && end != null) {
                records = queryService.handle(new GetSensorDataByDateRangeQuery(start, end));
            } 
            // Otherwise, get all records
            else {
                records = queryService.handle(new GetAllSensorDataQuery());
            }
            
            List<SensorDataResource> resources = SensorDataResourceAssembler.toResourceList(records);
            return ResponseEntity.ok(resources);
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid query parameters: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get sensor data for a specific device (sub-resource pattern)
     * GET /api/v1/analytics/devices/{deviceId}/data
     */
    @GetMapping("/devices/{deviceId}/data")
    @Operation(
            summary = "Get sensor data for a device",
            description = "Retrieve all sensor data records for a specific device."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sensor data"),
            @ApiResponse(responseCode = "404", description = "Device not found or no data available")
    })
    public ResponseEntity<List<SensorDataResource>> getDeviceSensorData(
            @Parameter(description = "Device identifier", example = "device_001")
            @PathVariable String deviceId) {
        
        log.debug("Retrieving sensor data for device: {}", deviceId);
        
        List<SensorDataRecord> records = queryService.handle(new GetSensorDataByDeviceIdQuery(deviceId));
        List<SensorDataResource> resources = SensorDataResourceAssembler.toResourceList(records);
        
        return ResponseEntity.ok(resources);
    }
}