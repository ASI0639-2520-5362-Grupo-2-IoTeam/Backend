package pe.iotteam.plantcare.subscription.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.iotteam.plantcare.subscription.domain.model.aggregates.Subscription;
import pe.iotteam.plantcare.subscription.domain.model.valueobjects.UserId;
import pe.iotteam.plantcare.subscription.infrastructure.persistence.jpa.repositories.SubscriptionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubscriptionQueryService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionQueryService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    /**
     * Retorna todas las suscripciones registradas.
     */
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    /**
     * Busca la suscripción de un usuario por su ID.
     */
    public Optional<Subscription> getByUserId(UUID userIdValue) {
        return subscriptionRepository.findByUserId(new UserId(userIdValue));
    }

    /**
     * Busca una suscripción por ID de suscripción.
     */
    public Optional<Subscription> getById(UUID subscriptionId) {
        return subscriptionRepository.findById(subscriptionId);
    }
}
