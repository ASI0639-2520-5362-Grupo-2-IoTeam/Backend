package pe.iotteam.plantcare.plant.interfaces.rest.resources;

import java.time.LocalDateTime;

public record WateringLogResource(
        Long id,
        Long plantId,
        LocalDateTime wateredAt
) {
}