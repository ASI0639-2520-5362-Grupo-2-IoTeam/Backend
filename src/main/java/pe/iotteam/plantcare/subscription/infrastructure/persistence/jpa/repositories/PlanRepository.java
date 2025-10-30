package pe.iotteam.plantcare.subscription.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.iotteam.plantcare.subscription.domain.model.entities.Plan;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlanRepository extends JpaRepository<Plan, UUID> {

    Optional<Plan> findByType(pe.iotteam.plantcare.subscription.domain.model.valueobjects.PlanType type);
}
