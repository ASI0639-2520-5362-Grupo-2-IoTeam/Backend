package pe.iotteam.plantcare.plant.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SensorData {
    @JsonProperty("air_temperature_celsius")
    private Integer airTemperatureCelsius;

    @JsonProperty("air_humidity_percent")
    private Double airHumidityPercent;

    @JsonProperty("luminosity_lux")
    private Integer luminosityLux;

    @JsonProperty("soil_moisture_percent")
    private Integer soilMoisturePercent;

    @JsonProperty("device_id")
    private String deviceId;

    // Convenience alias getters used by PlantController
    // These map the snake_case JSON fields to the semantic names the controller expects.
    public Integer getTemperature() {
        return this.airTemperatureCelsius;
    }

    public Double getHumidity() {
        return this.airHumidityPercent;
    }

    public Integer getLight() {
        return this.luminosityLux;
    }

    public Integer getSoilHumidity() {
        return this.soilMoisturePercent;
    }
}
