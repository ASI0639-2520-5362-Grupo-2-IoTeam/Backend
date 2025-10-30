package pe.iotteam.plantcare.subscription.infrastructure.persistence.jpa.repositories;

import pe.iotteam.plantcare.subscription.domain.model.aggregates.Subscription;
import pe.iotteam.plantcare.subscription.domain.model.valueobjects.UserId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository {

    Subscription save(Subscription subscription);

    Optional<Subscription> findById(UUID id);

    Optional<Subscription> findByUserId(UserId userId);

    List<Subscription> findAll();

    void deleteById(UUID id);

    void delete(Subscription subscription);
}
