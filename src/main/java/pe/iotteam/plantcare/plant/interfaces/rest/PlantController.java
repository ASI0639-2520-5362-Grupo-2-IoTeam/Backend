package pe.iotteam.plantcare.plant.interfaces.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pe.iotteam.plantcare.plant.application.internal.commandservices.PlantCommandService;
import pe.iotteam.plantcare.plant.application.internal.queryservices.PlantQueryService;
import pe.iotteam.plantcare.plant.domain.model.aggregates.Plant;
import pe.iotteam.plantcare.plant.domain.model.aggregates.PlantMetrics;
import pe.iotteam.plantcare.plant.domain.model.commands.DeletePlantCommand;
import pe.iotteam.plantcare.plant.domain.model.commands.WaterPlantCommand;
import pe.iotteam.plantcare.plant.domain.model.queries.GetPlantByIdQuery;
import pe.iotteam.plantcare.plant.domain.model.queries.GetPlantsByUserIdQuery;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantId;
import pe.iotteam.plantcare.plant.interfaces.rest.resources.*;
import pe.iotteam.plantcare.plant.interfaces.rest.transform.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Plant REST controller.
 * Exposes Plant-related HTTP endpoints for managing plants.
 */
@Tag(name = "Plant", description = "APIs to manage plants: create, read, update, delete, water and list by user")
@RestController("Plant")
@RequestMapping("/api/v1")
public final class PlantController {

    private static final Logger log = LoggerFactory.getLogger(PlantController.class);

    private final PlantCommandService commandService;
    private final PlantQueryService queryService;
    private final RestTemplate restTemplate;

    @Value("${edge.service.base-url}")
    private String edgeServiceUrl;

    public PlantController(PlantCommandService commandService, PlantQueryService queryService, RestTemplate restTemplate) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.restTemplate = restTemplate;
    }

    @Operation(summary = "Create a new plant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plant created", content = @Content(schema = @Schema(implementation = PlantResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/plants")
    public ResponseEntity<PlantResource> createPlant(@RequestBody CreatePlantResource resource) {
        var command = CreatePlantCommandFromResourceAssembler.toCommand(resource);
        var plant = commandService.handle(command);
        var plantResource = PlantResourceFromEntityAssembler.toResource(plant);
        return ResponseEntity.ok(plantResource);
    }

    @Operation(summary = "Get a plant by ID", description = "Get a plant by its ID. Includes live telemetry metrics when available.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plant found", content = @Content(schema = @Schema(implementation = PlantResource.class))),
            @ApiResponse(responseCode = "404", description = "Plant not found")
    })
    @GetMapping("/plants/{plantId}")
    public ResponseEntity<PlantResource> getById(@Parameter(description = "ID of the plant", required = true) @PathVariable Long plantId) {
        return queryService.handle(new GetPlantByIdQuery(plantId))
                .map(plant -> {
                    fetchLatestSensorData().ifPresent(sensorData -> enrichPlantWithLiveData(plant, sensorData));
                    var plantResource = PlantResourceFromEntityAssembler.toResource(plant);
                    return ResponseEntity.ok(plantResource);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get plants for a user", description = "Return all plants for a specific user, including live telemetry status.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of plants", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PlantResource.class))))
    })
    @GetMapping("/users/{userId}/plants")
    public ResponseEntity<List<PlantResource>> getByUserId(@Parameter(description = "ID of the user", required = true) @PathVariable UUID userId) {
        log.debug("GET /api/v1/users/{}/plants called", userId);
        // Use the query object provided by the application layer
        var plants = queryService.handle(new GetPlantsByUserIdQuery(userId));
        Optional<SensorData> latestSensorData = fetchLatestSensorData();

        var resources = plants.stream()
                .map(plant -> {
                    latestSensorData.ifPresent(sensorData -> enrichPlantWithLiveData(plant, sensorData));
                    return PlantResourceFromEntityAssembler.toResource(plant);
                })
                .collect(Collectors.toList());

        log.debug("Returning {} plants for userId={}", resources.size(), userId);
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Update a plant by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plant updated", content = @Content(schema = @Schema(implementation = PlantResource.class))),
            @ApiResponse(responseCode = "404", description = "Plant not found")
    })
    @PutMapping("/plants/{plantId}")
    public ResponseEntity<PlantResource> updatePlant(@Parameter(description = "ID of the plant to update", required = true) @PathVariable Long plantId,
                                                     @RequestBody UpdatePlantResource resource) {
        var command = UpdatePlantCommandFromResourceAssembler.toCommand(plantId, resource);
        var updatedPlant = commandService.handle(command);
        var plantResource = PlantResourceFromEntityAssembler.toResource(updatedPlant);
        return ResponseEntity.ok(plantResource);
    }

    @Operation(summary = "Water a specific plant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plant watered", content = @Content(schema = @Schema(implementation = PlantResource.class))),
            @ApiResponse(responseCode = "404", description = "Plant not found")
    })
    @PostMapping("/plants/{plantId}/watering")
    public ResponseEntity<PlantResource> waterPlant(@Parameter(description = "ID of the plant to water", required = true) @PathVariable Long plantId) {
        var command = new WaterPlantCommand(plantId);
        var updatedPlant = commandService.handle(command);
        var plantResource = PlantResourceFromEntityAssembler.toResource(updatedPlant);
        return ResponseEntity.ok(plantResource);
    }

    @Operation(summary = "Delete a plant by ID")
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

    /**
     * Fetches the latest sensor data from the edge service.
     * Supports responses that are either a JSON array or a single JSON object.
     * @return An Optional containing the latest SensorData, or empty if not available.
     */
    private Optional<SensorData> fetchLatestSensorData() {
        try {
            String json = restTemplate.getForObject(edgeServiceUrl, String.class);
            if (json == null || json.isBlank()) {
                return Optional.empty();
            }

            ObjectMapper mapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            try {
                SensorData[] array = mapper.readValue(json, SensorData[].class);
                if (array != null && array.length > 0) {
                    return Optional.ofNullable(array[0]);
                }
            } catch (JsonProcessingException ignored) {
                // Not an array, try single object
            }

            try {
                SensorData single = mapper.readValue(json, SensorData.class);
                return Optional.ofNullable(single);
            } catch (JsonProcessingException e) {
                log.error("Error parsing sensor data JSON from edge service at {}: {}", edgeServiceUrl, e.getMessage());
            }

        } catch (RestClientException e) {
            log.error("Error fetching sensor data from edge service at {}: {}", edgeServiceUrl, e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Enriches a Plant aggregate with live metrics from sensor data.
     * This method mutates the Plant object by adding a temporary metric and updating its status.
     * @param plant The Plant aggregate to enrich.
     * @param sensorData The live sensor data to use.
     */
    private void enrichPlantWithLiveData(Plant plant, SensorData sensorData) {
        Integer humidity = (sensorData.getHumidity() != null) ? sensorData.getHumidity().intValue() : null;

        var liveMetric = PlantMetrics.builder()
                .plantId(new PlantId(plant.getId()))
                .deviceId(sensorData.getDeviceId())
                .temperature(sensorData.getTemperature())
                .humidity(humidity)
                .light(sensorData.getLight())
                .soilHumidity(sensorData.getSoilHumidity())
                .build();

        plant.determineStatusFrom(liveMetric);
        plant.addMetric(liveMetric);
    }
}