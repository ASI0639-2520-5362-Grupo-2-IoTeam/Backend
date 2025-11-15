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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.iotteam.plantcare.analytics.application.internal.commandservices.SensorDataIngestionCommandService;
import pe.iotteam.plantcare.analytics.application.internal.queryservices.SensorDataQueryService;
import pe.iotteam.plantcare.analytics.domain.exceptions.DataIngestionException;
import pe.iotteam.plantcare.analytics.domain.model.aggregates.SensorDataRecord;
import pe.iotteam.plantcare.analytics.domain.model.commands.IngestSensorDataCommand;
import pe.iotteam.plantcare.analytics.domain.model.queries.GetAllSensorDataQuery;
import pe.iotteam.plantcare.analytics.domain.model.queries.GetSensorDataByDeviceIdQuery;
import pe.iotteam.plantcare.analytics.interfaces.rest.resources.IngestionResultResource;
import pe.iotteam.plantcare.analytics.interfaces.rest.resources.SensorDataResource;
import pe.iotteam.plantcare.analytics.interfaces.rest.transform.SensorDataResourceAssembler;

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
     * POST /api/v1/analytics/data-imports
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
     * GET /api/v1/analytics/sensor-data
     */
    @GetMapping("/data")
    @Operation(
            summary = "Get all sensor data",
            description = "Retrieve all sensor data records from analytics database."
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
     * Get sensor data for a specific device
     * GET /api/v1/analytics/devices/{deviceId}/sensor-data
     */
    @GetMapping("/devices/{deviceId}/data")
    @Operation(
            summary = "Get sensor data for a device",
            description = "Retrieve all sensor data records for a specific device."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sensor data")
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