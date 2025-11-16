package pe.iotteam.plantcare.plant.application.internal.queryservices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.iotteam.plantcare.plant.domain.model.aggregates.Plant;
import pe.iotteam.plantcare.plant.domain.model.queries.GetPlantByIdQuery;
import pe.iotteam.plantcare.plant.domain.model.queries.GetPlantsByUserIdQuery;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantId;
import pe.iotteam.plantcare.plant.infrastructure.persistence.jpa.repositories.PlantRepository;

import java.util.List;
import java.util.Optional;

/**
 * Query service for handling plant-related queries.
 * This service is responsible for retrieving plant data without modifying it.
 */
@Service
@RequiredArgsConstructor
public class PlantQueryService {

    private final PlantRepository plantRepository;

    /**
     * Handles the {@link GetPlantByIdQuery} to find a plant by its ID.
     * @param query The query containing the plant ID.
     * @return an {@link Optional} containing the {@link Plant} if found, otherwise empty.
     */
    public Optional<Plant> handle(GetPlantByIdQuery query) {
        return plantRepository.findById(new PlantId(query.plantId()));
    }

    /**
     * Handles the {@link GetPlantsByUserIdQuery} to find all plants belonging to a user.
     * @param query The query containing the user ID.
     * @return a list of {@link Plant}s, which may be empty if the user has no plants.
     */
    public List<Plant> handle(GetPlantsByUserIdQuery query) {
        return plantRepository.findByUserId(query.userId());
    }
}
