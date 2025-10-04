package pe.iotteam.plantcare.plant.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.iotteam.plantcare.plant.domain.model.aggregates.Plant;
import pe.iotteam.plantcare.plant.domain.model.queries.GetPlantByIdQuery;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantId;
import pe.iotteam.plantcare.plant.infrastructure.persistence.jpa.repositories.PlantRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlantQueryService {

    private final PlantRepository plantRepository;

    public PlantQueryService(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    public Optional<Plant> handle(GetPlantByIdQuery query) {
        return plantRepository.findById(new PlantId(query.plantId()));
    }

    public List<Plant> handleFindByUser(UUID userId) {
        return plantRepository.findByUserId(userId);
    }
}