package pe.iotteam.plantcare.plant.interfaces.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pe.iotteam.plantcare.plant.application.internal.commandservices.PlantCommandService;
import pe.iotteam.plantcare.plant.application.internal.queryservices.PlantQueryService;
import pe.iotteam.plantcare.plant.domain.model.aggregates.PlantMetrics;
import pe.iotteam.plantcare.plant.domain.model.commands.DeletePlantCommand;
import pe.iotteam.plantcare.plant.domain.model.commands.WaterPlantCommand;
import pe.iotteam.plantcare.plant.domain.model.queries.GetPlantByIdQuery;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantId;
import pe.iotteam.plantcare.plant.interfaces.rest.resources.*;
import pe.iotteam.plantcare.plant.interfaces.rest.transform.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

// Added for logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// OpenAPI/Swagger annotations
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.Parameter;

/**
 * Plant REST controller.
 *
 * This controller exposes the Plant-related HTTP endpoints. It is registered as the
 * bean name "Plant" so documentation tools or beans that display controller names
 * can show "Plant" instead of the default controller name.
 *
 * Endpoints (summary):
 *
 * POST    /api/v1/plants                      -> Create a new plant
 * GET     /api/v1/plants/{plantId}            -> Get a plant by ID
 * PUT     /api/v1/plants/{plantId}            -> Update a plant by ID
 * DELETE  /api/v1/plants/{plantId}            -> Delete a plant by ID
 * POST    /api/v1/plants/{plantId}/watering   -> Water a specific plant (action)
 * GET     /api/v1/users/{userId}/plants       -> Get all plants for a specific user
 */
@Tag(name = "Plant", description = "APIs to manage plants: create, read, update, delete, water and list by user")
@RestController("Plant")
@RequestMapping("/api/v1")
public class PlantController {

    private final PlantCommandService commandService;
    private final PlantQueryService queryService;
    private final RestTemplate restTemplate;

    @Value("${edge.service.base-url}")
    private String edgeServiceUrl;

    private static final Logger log = LoggerFactory.getLogger(PlantController.class);

    public PlantController(PlantCommandService commandService, PlantQueryService queryService, RestTemplate restTemplate) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.restTemplate = restTemplate;
    }

    // Create plant
    @Operation(summary = "Create a new plant", description = "Create a new plant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plant created",
                    content = @Content(schema = @Schema(implementation = PlantResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/plants")
    public ResponseEntity<PlantResource> createPlant(@RequestBody CreatePlantResource resource) {
        var command = CreatePlantCommandFromResourceAssembler.toCommand(resource);
        var plant = commandService.handle(command);
        return ResponseEntity.ok(PlantResourceFromEntityAssembler.toResource(plant));
    }

    // Get plant by ID with computed status (includes live telemetry)
    @Operation(summary = "Get a plant by ID", description = "Get a plant by its ID. Includes live telemetry metrics when available.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plant found",
                    content = @Content(schema = @Schema(implementation = PlantResource.class))),
            @ApiResponse(responseCode = "404", description = "Plant not found")
    })
    @GetMapping("/plants/{plantId}")
    public ResponseEntity<PlantResource> getById(@Parameter(description = "ID of the plant", required = true) @PathVariable Long plantId) {
        var query = new GetPlantByIdQuery(plantId);
        return queryService.handle(query)
                .map(plant -> {
                    // 1. Fetch live telemetry once
                    ResponseEntity<SensorData[]> response = restTemplate.getForEntity(edgeServiceUrl, SensorData[].class);
                    SensorData latestSensorData = response.getBody() != null && response.getBody().length > 0 ? response.getBody()[0] : new SensorData();

                    // 2. Create a temporary PlantMetrics object
                    var liveMetric = new PlantMetrics(
                            new PlantId(plant.getId()),
                            latestSensorData.getDeviceId(),
                            latestSensorData.getTemperature(),
                            latestSensorData.getHumidity() != null ? latestSensorData.getHumidity().intValue() : 0,
                            latestSensorData.getLight(),
                            latestSensorData.getSoilHumidity()
                    );

                    // 3. Determine plant status from live metric
                    plant.determineStatusFrom(liveMetric);

                    // 4. Add live metric to the plant for the JSON response
                    plant.addMetric(liveMetric);

                    // 5. Build the response
                    var resource = PlantResourceFromEntityAssembler.toResource(plant);
                    return ResponseEntity.ok(resource);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Get plants by user ID (returns plants with computed status using live telemetry)
    @Operation(summary = "Get plants for a user", description = "Return all plants for a specific user. Each plant includes computed status using live telemetry when available.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of plants",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = PlantResource.class)))),
            @ApiResponse(responseCode = "404", description = "User not found or no plants")
    })
    @GetMapping("/users/{userId}/plants")
    public ResponseEntity<List<PlantResource>> getByUserId(@Parameter(description = "ID of the user", required = true) @PathVariable UUID userId) {
        log.debug("GET /api/v1/users/{}/plants called", userId);
        // 1. Fetch live telemetry once
        ResponseEntity<SensorData[]> response = restTemplate.getForEntity(edgeServiceUrl, SensorData[].class);
        SensorData latestSensorData = response.getBody() != null && response.getBody().length > 0 ? response.getBody()[0] : new SensorData();

        // 2. Get all plants for the user
        var plants = queryService.handleFindByUser(userId);
        log.debug("queryService.handleFindByUser returned {} plants for userId={}", plants.size(), userId);

        // 3. For each plant, attach telemetry and compute status
        var resources = plants.stream()
                .map(plant -> {
                    var liveMetric = new PlantMetrics(
                            new PlantId(plant.getId()),
                            latestSensorData.getDeviceId(),
                            latestSensorData.getTemperature(),
                            latestSensorData.getHumidity() != null ? latestSensorData.getHumidity().intValue() : 0,
                            latestSensorData.getLight(),
                            latestSensorData.getSoilHumidity()
                    );
                    plant.determineStatusFrom(liveMetric);
                    plant.addMetric(liveMetric);
                    return PlantResourceFromEntityAssembler.toResource(plant);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(resources);
    }

    // Update plant
    @Operation(summary = "Update a plant by ID", description = "Update the plant identified by the given ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plant updated",
                    content = @Content(schema = @Schema(implementation = PlantResource.class))),
            @ApiResponse(responseCode = "404", description = "Plant not found")
    })
    @PutMapping("/plants/{plantId}")
    public ResponseEntity<PlantResource> updatePlant(@Parameter(description = "ID of the plant to update", required = true) @PathVariable Long plantId,
                                                     @RequestBody UpdatePlantResource resource) {
        var command = UpdatePlantCommandFromResourceAssembler.toCommand(plantId, resource);
        var updatedPlant = commandService.handle(command);
        return ResponseEntity.ok(PlantResourceFromEntityAssembler.toResource(updatedPlant));
    }

    // Mark as watered
    @Operation(summary = "Water a specific plant", description = "Mark a plant as watered (action).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plant watered",
                    content = @Content(schema = @Schema(implementation = PlantResource.class))),
            @ApiResponse(responseCode = "404", description = "Plant not found")
    })
    @PostMapping("/plants/{plantId}/watering")
    public ResponseEntity<PlantResource> waterPlant(@Parameter(description = "ID of the plant to water", required = true) @PathVariable Long plantId) {
        var command = new WaterPlantCommand(plantId);
        var updatedPlant = commandService.handle(command);
        return ResponseEntity.ok(PlantResourceFromEntityAssembler.toResource(updatedPlant));
    }

    // Delete plant
    @Operation(summary = "Delete a plant by ID", description = "Delete the plant identified by the given ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Plant deleted"),
            @ApiResponse(responseCode = "404", description = "Plant not found")
    })
    @DeleteMapping("/plants/{plantId}")
    public ResponseEntity<Void> deletePlant(@Parameter(description = "ID of the plant to delete", required = true) @PathVariable Long plantId) {
        var command = new DeletePlantCommand(plantId);
        commandService.handle(command);
        return ResponseEntity.noContent().build();
    }
}