package pe.iotteam.plantcare.analytics.infrastructure.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for sensor data from external Edge Service API
 * Matches the JSON structure returned by the Flasgger API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalSensorDataDto {
    
    @JsonProperty("created_at")
    @JsonDeserialize(using = Rfc1123DateTimeDeserializer.class)
    private LocalDateTime createdAt;
    
    @JsonProperty("device_id")
    private String deviceId;
    
    @JsonProperty("humidity")
    private Integer humidity;
    
    @JsonProperty("light")
    private Integer light;
    
    @JsonProperty("soil_humidity")
    private Integer soilHumidity;
    
    @JsonProperty("temperature")
    private Integer temperature;
}
