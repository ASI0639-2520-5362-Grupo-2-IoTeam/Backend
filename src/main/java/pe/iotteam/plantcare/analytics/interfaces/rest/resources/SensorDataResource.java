package pe.iotteam.plantcare.analytics.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * Resource DTO for sensor data record responses
 */
public record SensorDataResource(
        @JsonProperty("id")
        Long id,
        
        @JsonProperty("device_id")
        String deviceId,
        
        @JsonProperty("temperature")
        Integer temperature,
        
        @JsonProperty("humidity")
        Integer humidity,
        
        @JsonProperty("light")
        Integer light,
        
        @JsonProperty("soil_humidity")
        Integer soilHumidity,
        
        @JsonProperty("created_at")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt
) {
}
