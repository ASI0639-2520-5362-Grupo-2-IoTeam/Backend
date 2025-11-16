package pe.iotteam.plantcare.plant.domain.model.aggregates;

import lombok.Getter;
import pe.iotteam.plantcare.plant.domain.model.commands.UpdatePlantCommand;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantId;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantStatus;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.UserId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Plant {

    // Watering Calculation Constants
    private static final long BASE_WATERING_INTERVAL_HOURS = 72;
    private static final long TEMP_ADJUSTMENT_HOURS = 12;
    private static final long HUMIDITY_ADJUSTMENT_HOURS = 8;
    private static final int HIGH_TEMP_THRESHOLD = 25;
    private static final int LOW_TEMP_THRESHOLD = 18;
    private static final int LOW_HUMIDITY_THRESHOLD = 40;
    private static final int HIGH_HUMIDITY_THRESHOLD = 60;

    // Status Thresholds
    private static final int CRITICAL_TEMP_MIN = 10;
    private static final int CRITICAL_TEMP_MAX = 35;
    private static final int CRITICAL_HUMIDITY_MIN = 30;
    private static final int CRITICAL_HUMIDITY_MAX = 70;
    private static final int CRITICAL_SOIL_HUMIDITY_MIN = 20;
    private static final int CRITICAL_SOIL_HUMIDITY_MAX = 80;

    private static final int WARNING_TEMP_MIN = 18;
    private static final int WARNING_TEMP_MAX = 26;
    private static final int WARNING_HUMIDITY_MIN = 40;
    private static final int WARNING_HUMIDITY_MAX = 60;
    private static final int WARNING_SOIL_HUMIDITY_MIN = 30;
    private static final int WARNING_SOIL_HUMIDITY_MAX = 70;


    private Long id;
    private final UserId userId;
    private String name;
    private String type;
    private String imgUrl;
    private String bio;
    private String location;
    private PlantStatus status;
    private final List<PlantMetrics> metrics;
    private final List<WateringLog> wateringLogs;
    private LocalDateTime lastWatered;
    private LocalDateTime nextWatering;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Constructor for creating a new Plant with default values.
     */
    public Plant(UserId userId, String name, String type, String imgUrl, String bio, String location) {
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.imgUrl = imgUrl;
        this.bio = bio;
        this.location = location;
        this.status = PlantStatus.HEALTHY;
        this.metrics = new ArrayList<>();
        this.wateringLogs = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Private constructor for the builder to reconstruct a Plant from data source.
     */
    private Plant(PlantBuilder builder) {
        this.id = builder.id;
        this.userId = builder.userId;
        this.name = builder.name;
        this.type = builder.type;
        this.imgUrl = builder.imgUrl;
        this.bio = builder.bio;
        this.location = builder.location;
        this.status = builder.status;
        this.lastWatered = builder.lastWatered;
        this.nextWatering = builder.nextWatering;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.metrics = builder.metrics;
        this.wateringLogs = builder.wateringLogs;
    }

    public static PlantBuilder builder() {
        return new PlantBuilder();
    }

    public void update(UpdatePlantCommand command) {
        this.name = command.name();
        this.type = command.type();
        this.imgUrl = command.imgUrl();
        this.bio = command.bio();
        this.location = command.location();
        this.updatedAt = LocalDateTime.now();
    }

    public void addMetric(PlantMetrics metric) {
        this.metrics.add(metric);
    }

    public void determineStatusFrom(PlantMetrics latestMetrics) {
        if (isCritical(latestMetrics)) {
            this.status = PlantStatus.CRITICAL;
        } else if (isWarning(latestMetrics)) {
            this.status = PlantStatus.WARNING;
        } else {
            this.status = PlantStatus.HEALTHY;
        }
    }

    /**
     * Records a watering event and calculates the next watering time.
     * @param latestMetrics The current sensor data, used to calculate the next watering time.
     */
    public void water(PlantMetrics latestMetrics) {
        this.lastWatered = LocalDateTime.now();
        this.wateringLogs.add(new WateringLog(new PlantId(this.id)));
        this.nextWatering = calculateNextWatering(latestMetrics);
        this.updatedAt = LocalDateTime.now();
    }

    private LocalDateTime calculateNextWatering(PlantMetrics metrics) {
        long adjustedHours = BASE_WATERING_INTERVAL_HOURS;

        if (metrics.getTemperature() > HIGH_TEMP_THRESHOLD) {
            adjustedHours -= TEMP_ADJUSTMENT_HOURS;
        } else if (metrics.getTemperature() < LOW_TEMP_THRESHOLD) {
            adjustedHours += TEMP_ADJUSTMENT_HOURS;
        }

        if (metrics.getHumidity() < LOW_HUMIDITY_THRESHOLD) {
            adjustedHours -= HUMIDITY_ADJUSTMENT_HOURS;
        } else if (metrics.getHumidity() > HIGH_HUMIDITY_THRESHOLD) {
            adjustedHours += HUMIDITY_ADJUSTMENT_HOURS;
        }

        return this.lastWatered.plusHours(adjustedHours);
    }

    private boolean isCritical(PlantMetrics metrics) {
        return metrics.getTemperature() < CRITICAL_TEMP_MIN || metrics.getTemperature() > CRITICAL_TEMP_MAX ||
               metrics.getHumidity() < CRITICAL_HUMIDITY_MIN || metrics.getHumidity() > CRITICAL_HUMIDITY_MAX ||
               metrics.getSoilHumidity() < CRITICAL_SOIL_HUMIDITY_MIN || metrics.getSoilHumidity() > CRITICAL_SOIL_HUMIDITY_MAX;
    }

    private boolean isWarning(PlantMetrics metrics) {
        return metrics.getTemperature() < WARNING_TEMP_MIN || metrics.getTemperature() > WARNING_TEMP_MAX ||
               metrics.getHumidity() < WARNING_HUMIDITY_MIN || metrics.getHumidity() > WARNING_HUMIDITY_MAX ||
               metrics.getSoilHumidity() < WARNING_SOIL_HUMIDITY_MIN || metrics.getSoilHumidity() > WARNING_SOIL_HUMIDITY_MAX;
    }

    /**
     * Builder class for constructing Plant instances.
     */
    public static class PlantBuilder {
        private Long id;
        private UserId userId;
        private String name;
        private String type;
        private String imgUrl;
        private String bio;
        private String location;
        private PlantStatus status = PlantStatus.HEALTHY;
        private List<PlantMetrics> metrics = new ArrayList<>();
        private List<WateringLog> wateringLogs = new ArrayList<>();
        private LocalDateTime lastWatered;
        private LocalDateTime nextWatering;
        private LocalDateTime createdAt = LocalDateTime.now();
        private LocalDateTime updatedAt = LocalDateTime.now();

        public PlantBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public PlantBuilder userId(UserId userId) {
            this.userId = userId;
            return this;
        }

        public PlantBuilder name(String name) {
            this.name = name;
            return this;
        }

        public PlantBuilder type(String type) {
            this.type = type;
            return this;
        }

        public PlantBuilder imgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
            return this;
        }

        public PlantBuilder bio(String bio) {
            this.bio = bio;
            return this;
        }

        public PlantBuilder location(String location) {
            this.location = location;
            return this;
        }

        public PlantBuilder status(PlantStatus status) {
            if (status != null) this.status = status;
            return this;
        }

        public PlantBuilder metrics(List<PlantMetrics> metrics) {
            if (metrics != null) this.metrics = metrics;
            return this;
        }

        public PlantBuilder wateringLogs(List<WateringLog> wateringLogs) {
            if (wateringLogs != null) this.wateringLogs = wateringLogs;
            return this;
        }

        public PlantBuilder lastWatered(LocalDateTime lastWatered) {
            this.lastWatered = lastWatered;
            return this;
        }

        public PlantBuilder nextWatering(LocalDateTime nextWatering) {
            this.nextWatering = nextWatering;
            return this;
        }

        public PlantBuilder createdAt(LocalDateTime createdAt) {
            if (createdAt != null) this.createdAt = createdAt;
            return this;
        }

        public PlantBuilder updatedAt(LocalDateTime updatedAt) {
            if (updatedAt != null) this.updatedAt = updatedAt;
            return this;
        }

        public Plant build() {
            return new Plant(this);
        }
    }
}