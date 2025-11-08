package pe.iotteam.plantcare.plant.interfaces.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import pe.iotteam.plantcare.plant.application.internal.queryservices.PlantQueryService;
import pe.iotteam.plantcare.plant.domain.model.aggregates.PlantMetrics;
import pe.iotteam.plantcare.plant.domain.model.entities.PlantEntity;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantId;
import pe.iotteam.plantcare.plant.infrastructure.persistence.jpa.repositories.PlantJpaRepository;
import pe.iotteam.plantcare.plant.interfaces.rest.resources.SensorData;
import pe.iotteam.plantcare.plant.interfaces.rest.resources.PlantResource;
import pe.iotteam.plantcare.plant.interfaces.rest.transform.PlantResourceFromEntityAssembler;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Compatibility controller to support legacy frontend calls to /api/plants?userId=...
 * This delegates to the same query service and mapping logic used by PlantController.
 */
@RestController
@RequestMapping("/api")
public class PlantCompatController {

    private static final Logger log = LoggerFactory.getLogger(PlantCompatController.class);

    private final PlantQueryService queryService;
    private final RestTemplate restTemplate;
    private final PlantJpaRepository plantJpaRepository; // injected for debug

    @Value("${edge.service.base-url}")
    private String edgeServiceUrl;

    public PlantCompatController(PlantQueryService queryService, RestTemplate restTemplate, PlantJpaRepository plantJpaRepository) {
        this.queryService = queryService;
        this.restTemplate = restTemplate;
        this.plantJpaRepository = plantJpaRepository;
    }

    @GetMapping("/plants")
    public ResponseEntity<List<PlantResource>> getPlantsByUserQuery(@RequestParam UUID userId) {
        log.debug("Compatibility GET /api/plants?userId={} called", userId);

        ResponseEntity<SensorData[]> response = restTemplate.getForEntity(edgeServiceUrl, SensorData[].class);
        SensorData latestSensorData = response.getBody() != null && response.getBody().length > 0 ? response.getBody()[0] : new SensorData();

        var plants = queryService.handleFindByUser(userId);
        log.debug("Compatibility: queryService.handleFindByUser returned {} plants for userId={}", plants.size(), userId);

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

    // Debug endpoint: devuelve id y userId de las filas encontradas para inspección rápida
    @GetMapping("/debug/plants")
    public ResponseEntity<List<Map<String, Object>>> debugFindByUser(@RequestParam String userId) {
        log.debug("Debug GET /api/debug/plants?userId={} called", userId);
        List<PlantEntity> entities = plantJpaRepository.findByUserId(userId);
        var result = entities.stream()
                .map(e -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", e.getId());
                    m.put("userIdInDb", e.getUserId());
                    m.put("name", e.getName());
                    return m;
                })
                .collect(Collectors.toList());
        log.debug("Debug: plantJpaRepository.findByUserId returned {} entities for userId={}", result.size(), userId);
        return ResponseEntity.ok(result);
    }

    // Debug endpoint: devuelve TODAS las plantas (sin filtro) para ver qué hay en la BD
    @GetMapping("/debug/plants/all")
    public ResponseEntity<List<Map<String, Object>>> debugAllPlants() {
        log.debug("Debug GET /api/debug/plants/all called");
        List<PlantEntity> allEntities = plantJpaRepository.findAll();
        var result = allEntities.stream()
                .map(e -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", e.getId());
                    m.put("userIdInDb", e.getUserId());
                    m.put("name", e.getName());
                    m.put("type", e.getType());
                    return m;
                })
                .collect(Collectors.toList());
        log.debug("Debug: Total plants in database: {}", result.size());
        return ResponseEntity.ok(result);
    }

    // Debug endpoint: crear planta rápida sin autenticación (solo para desarrollo)
    @org.springframework.web.bind.annotation.PostMapping("/debug/plants/create")
    public ResponseEntity<Map<String, Object>> debugCreatePlant(
            @RequestParam String userId,
            @RequestParam String name,
            @RequestParam(defaultValue = "Planta") String type,
            @RequestParam(defaultValue = "https://example.com/plant.jpg") String imgUrl,
            @RequestParam(defaultValue = "Planta de prueba") String bio,
            @RequestParam(defaultValue = "Casa") String location) {

        log.debug("Debug POST /api/debug/plants/create called with userId={}", userId);

        PlantEntity entity = new PlantEntity();
        entity.setUserId(userId);
        entity.setName(name);
        entity.setType(type);
        entity.setImgUrl(imgUrl);
        entity.setBio(bio);
        entity.setLocation(location);
        entity.setStatus("HEALTHY");
        entity.setCreatedAt(java.time.LocalDateTime.now());
        entity.setUpdatedAt(java.time.LocalDateTime.now());

        PlantEntity saved = plantJpaRepository.save(entity);

        Map<String, Object> result = new HashMap<>();
        result.put("id", saved.getId());
        result.put("userId", saved.getUserId());
        result.put("name", saved.getName());
        result.put("type", saved.getType());
        result.put("message", "Plant created successfully");

        log.debug("Debug: Created plant with id={} for userId={}", saved.getId(), userId);
        return ResponseEntity.ok(result);
    }
}
