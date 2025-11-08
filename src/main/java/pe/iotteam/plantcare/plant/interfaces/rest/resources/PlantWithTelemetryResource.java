package pe.iotteam.plantcare.plant.interfaces.rest.resources;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlantWithTelemetryResource {
    private Long id;
    private UUID userId;
    private String name;
    private String type;
    private String imgUrl;
    private String bio;
    private String location;
    private PlantStatus status;
    private LocalDateTime lastWatered;
    private LocalDateTime nextWatering;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer temperature;
    private Double humidity;
    private Integer light;
    private Integer soil_humidity;
    private String deviceId;
}
