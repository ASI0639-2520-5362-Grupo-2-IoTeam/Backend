package pe.iotteam.plantcare.plant.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SensorData {
    private Integer temperature;
    private Double humidity;
    private Integer light;
    @JsonProperty("soil_humidity")
    private Integer soilHumidity;
    @JsonProperty("device_id")
    private String deviceId;
}