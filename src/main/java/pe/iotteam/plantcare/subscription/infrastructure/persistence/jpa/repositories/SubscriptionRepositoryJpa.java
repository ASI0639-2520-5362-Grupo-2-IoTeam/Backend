package pe.iotteam.plantcare.subscription.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.iotteam.plantcare.subscription.domain.model.aggregates.Subscription;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepositoryJpa extends JpaRepository<Subscription, UUID> {

    Optional<Subscription> findByUserId_Value(UUID value);
}
