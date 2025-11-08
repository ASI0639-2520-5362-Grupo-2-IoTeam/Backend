package pe.iotteam.plantcare.plant.interfaces.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pe.iotteam.plantcare.plant.application.internal.commandservices.PlantCommandService;
import pe.iotteam.plantcare.plant.application.internal.queryservices.PlantQueryService;
import pe.iotteam.plantcare.plant.domain.model.commands.DeletePlantCommand;
import pe.iotteam.plantcare.plant.domain.model.queries.GetPlantByIdQuery;
import pe.iotteam.plantcare.plant.interfaces.rest.resources.*;
import pe.iotteam.plantcare.plant.interfaces.rest.transform.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/plants")
public class PlantController {

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

    // Crear planta
    @PostMapping
    public ResponseEntity<PlantResource> createPlant(@RequestBody CreatePlantResource resource) {
        var command = CreatePlantCommandFromResourceAssembler.toCommand(resource);
        var plant = commandService.handle(command);
        return ResponseEntity.ok(PlantResourceFromEntityAssembler.toResource(plant));
    }

    // Obtener planta por ID
    @GetMapping("/{plantId}")
    public ResponseEntity<PlantWithTelemetryResource> getById(@PathVariable Long plantId) {
        var query = new GetPlantByIdQuery(plantId);
        return queryService.handle(query)
                .map(plant -> {
                    ResponseEntity<SensorData[]> response = restTemplate.getForEntity(edgeServiceUrl, SensorData[].class);
                    SensorData latestSensorData = response.getBody() != null && response.getBody().length > 0 ? response.getBody()[0] : new SensorData();
                    var resource = PlantWithTelemetryResourceFromEntityAssembler.toResource(plant, latestSensorData.getTemperature(), latestSensorData.getHumidity(), latestSensorData.getLight(), latestSensorData.getSoilHumidity(), latestSensorData.getDeviceId());
                    return ResponseEntity.ok(resource);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Obtener plantas por usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlantResource>> getByUserId(@PathVariable UUID userId) {
        var plants = queryService.handleFindByUser(userId);
        var resources = plants.stream()
                .map(PlantResourceFromEntityAssembler::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    // Actualizar planta
    @PutMapping("/{plantId}")
    public ResponseEntity<PlantResource> updatePlant(@PathVariable Long plantId,
                                                     @RequestBody UpdatePlantResource resource) {
        var command = UpdatePlantCommandFromResourceAssembler.toCommand(plantId, resource);
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