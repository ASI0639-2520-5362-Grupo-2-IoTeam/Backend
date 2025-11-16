package pe.iotteam.plantcare.plant.domain.model.queries;

import java.util.UUID;

/**
 * Query to retrieve all plants associated with a specific user.
 * @param userId The unique identifier of the user.
 */
public record GetPlantsByUserIdQuery(UUID userId) {
}
