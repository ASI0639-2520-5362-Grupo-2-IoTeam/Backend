package pe.iotteam.plantcare.plant.domain.model.aggregates;

import lombok.Getter;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantId;

import java.time.LocalDateTime;

@Getter
public class PlantMetrics {
    private Long id;
    private final PlantId plantId;
    private final String deviceId;
    private final Integer temperature;
    private final Integer humidity;
    private final Integer light;
    private final Integer soilHumidity;
    private final LocalDateTime createdAt;

    /**
     * Private constructor to be used by the builder.
     */
    private PlantMetrics(PlantMetricsBuilder builder) {
        this.id = builder.id;
        this.plantId = builder.plantId;
        this.deviceId = builder.deviceId;
        this.temperature = builder.temperature;
        this.humidity = builder.humidity;
        this.light = builder.light;
        this.soilHumidity = builder.soilHumidity;
        this.createdAt = builder.createdAt;
    }

    /**
     * Provides a new builder instance.
     * @return A new PlantMetricsBuilder.
     */
    public static PlantMetricsBuilder builder() {
        return new PlantMetricsBuilder();
    }

    /**
     * Builder class for constructing PlantMetrics instances.
     */
    public static class PlantMetricsBuilder {
        private Long id;
        private PlantId plantId;
        private String deviceId;
        private Integer temperature;
        private Integer humidity;
        private Integer light;
        private Integer soilHumidity;
        private LocalDateTime createdAt = LocalDateTime.now(); // Default to now

        public PlantMetricsBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public PlantMetricsBuilder plantId(PlantId plantId) {
            this.plantId = plantId;
            return this;
        }

        public PlantMetricsBuilder deviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public PlantMetricsBuilder temperature(Integer temperature) {
            this.temperature = temperature;
            return this;
        }

        public PlantMetricsBuilder humidity(Integer humidity) {
            this.humidity = humidity;
            return this;
        }

        public PlantMetricsBuilder light(Integer light) {
            this.light = light;
            return this;
        }

        public PlantMetricsBuilder soilHumidity(Integer soilHumidity) {
            this.soilHumidity = soilHumidity;
            return this;
        }

        /**
         * Overrides the default createdAt time. Use when reconstructing from persistence.
         */
        public PlantMetricsBuilder createdAt(LocalDateTime createdAt) {
            if (createdAt != null) {
                this.createdAt = createdAt;
            }
            return this;
        }

        public PlantMetrics build() {
            // Here you could add validation if needed, e.g., ensuring non-null fields.
            return new PlantMetrics(this);
        }
    }
}