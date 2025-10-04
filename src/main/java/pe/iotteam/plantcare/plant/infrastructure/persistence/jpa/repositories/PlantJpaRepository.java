package pe.iotteam.plantcare.plant.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.iotteam.plantcare.plant.domain.model.entities.PlantEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlantJpaRepository extends JpaRepository<PlantEntity, UUID> {

    Optional<PlantEntity> findById(UUID id);

    List<PlantEntity> findByUserId_Value(UUID userId);
}