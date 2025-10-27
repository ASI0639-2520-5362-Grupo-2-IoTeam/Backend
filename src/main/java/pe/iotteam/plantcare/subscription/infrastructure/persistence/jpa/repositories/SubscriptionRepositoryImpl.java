package pe.iotteam.plantcare.subscription.infrastructure.persistence.jpa.repositories;

import org.springframework.stereotype.Repository;
import pe.iotteam.plantcare.subscription.domain.model.aggregates.Subscription;
import pe.iotteam.plantcare.subscription.domain.model.valueobjects.UserId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    private final SubscriptionRepositoryJpa jpaRepository;

    public SubscriptionRepositoryImpl(SubscriptionRepositoryJpa jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Subscription save(Subscription subscription) {
        return jpaRepository.save(subscription);
    }

    @Override
    public Optional<Subscription> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Subscription> findByUserId(UserId userId) {
        return jpaRepository.findByUserId_Value(userId.value());
    }

    @Override
    public List<Subscription> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void delete(Subscription subscription) {
        jpaRepository.delete(subscription);
    }
}
