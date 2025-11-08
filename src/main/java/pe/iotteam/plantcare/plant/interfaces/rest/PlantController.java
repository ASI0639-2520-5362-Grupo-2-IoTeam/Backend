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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

// Añadidos para logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/plants")
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

    // Crear planta
    @PostMapping
    public ResponseEntity<PlantResource> createPlant(@RequestBody CreatePlantResource resource) {
        var command = CreatePlantCommandFromResourceAssembler.toCommand(resource);
        var plant = commandService.handle(command);
        return ResponseEntity.ok(PlantResourceFromEntityAssembler.toResource(plant));
    }

    // Obtener planta por ID con estado calculado
    @GetMapping("/{plantId}")
    public ResponseEntity<PlantResource> getById(@PathVariable Long plantId) {
        var query = new GetPlantByIdQuery(plantId);
        return queryService.handle(query)
                .map(plant -> {
                    // 1. Obtener datos de telemetría en vivo
                    ResponseEntity<SensorData[]> response = restTemplate.getForEntity(edgeServiceUrl, SensorData[].class);
                    SensorData latestSensorData = response.getBody() != null && response.getBody().length > 0 ? response.getBody()[0] : new SensorData();

                    // 2. Crear un objeto PlantMetrics temporal
                    var liveMetric = new PlantMetrics(
                            new PlantId(plant.getId()),
                            latestSensorData.getDeviceId(),
                            latestSensorData.getTemperature(),
                            latestSensorData.getHumidity() != null ? latestSensorData.getHumidity().intValue() : 0,
                            latestSensorData.getLight(),
                            latestSensorData.getSoilHumidity()
                    );

                    // 3. Determinar el estado de la planta basado en la métrica en vivo
                    plant.determineStatusFrom(liveMetric);

                    // 4. Añadir la métrica en vivo a la lista para la respuesta JSON
                    plant.addMetric(liveMetric);

                    // 5. Crear la respuesta
                    var resource = PlantResourceFromEntityAssembler.toResource(plant);
                    return ResponseEntity.ok(resource);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Obtener plantas por usuario con estado calculado
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlantResource>> getByUserId(@PathVariable UUID userId) {
        log.debug("GET /api/v1/plants/user/{} called", userId);
        // 1. Obtener datos de telemetría en vivo (una sola vez)
        ResponseEntity<SensorData[]> response = restTemplate.getForEntity(edgeServiceUrl, SensorData[].class);
        SensorData latestSensorData = response.getBody() != null && response.getBody().length > 0 ? response.getBody()[0] : new SensorData();

        // 2. Obtener todas las plantas del usuario
        var plants = queryService.handleFindByUser(userId);
        log.debug("queryService.handleFindByUser returned {} plants for userId={}", plants.size(), userId);

        // 3. Para cada planta, añadir la telemetría y calcular el estado
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

    // Ruta adicional compatible con la convención /api/v1/users/{userId}/plants
    @GetMapping(path = "/users/{userId}/plants")
    public ResponseEntity<List<PlantResource>> getByUserIdAlternate(@PathVariable UUID userId) {
        log.debug("GET /api/v1/users/{}/plants called - delegating to /api/v1/plants/user/{}", userId, userId);
        return getByUserId(userId);
    }

    // Actualizar planta
    @PutMapping("/{plantId}")
    public ResponseEntity<PlantResource> updatePlant(@PathVariable Long plantId,
                                                     @RequestBody UpdatePlantResource resource) {
        var command = UpdatePlantCommandFromResourceAssembler.toCommand(plantId, resource);
        var updatedPlant = commandService.handle(command);
        return ResponseEntity.ok(PlantResourceFromEntityAssembler.toResource(updatedPlant));
    }

    // Marcar como regada
    @PostMapping("/{plantId}/water")
    public ResponseEntity<PlantResource> waterPlant(@PathVariable Long plantId) {
        var command = new WaterPlantCommand(plantId);
        var updatedPlant = commandService.handle(command);
        return ResponseEntity.ok(PlantResourceFromEntityAssembler.toResource(updatedPlant));
    }

    // Eliminar planta
    @DeleteMapping("/{plantId}")
    public ResponseEntity<Void> deletePlant(@PathVariable Long plantId) {
        var command = new DeletePlantCommand(plantId);
        commandService.handle(command);
        return ResponseEntity.noContent().build();
    }
}