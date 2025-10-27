package pe.iotteam.plantcare.subscription.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.iotteam.plantcare.subscription.domain.exceptions.InvalidSubscriptionStateException;
import pe.iotteam.plantcare.subscription.domain.model.aggregates.Subscription;
import pe.iotteam.plantcare.subscription.domain.model.valueobjects.PlanType;
import pe.iotteam.plantcare.subscription.domain.model.valueobjects.UserId;
import pe.iotteam.plantcare.subscription.domain.model.valueobjects.SubscriptionStatus;
import pe.iotteam.plantcare.subscription.infrastructure.persistence.jpa.repositories.PlanRepository;
import pe.iotteam.plantcare.subscription.infrastructure.persistence.jpa.repositories.SubscriptionRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class SubscriptionCommandService {

    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;

    public SubscriptionCommandService(SubscriptionRepository subscriptionRepository,
                                      PlanRepository planRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
    }

    /**
     * Suscribe o cambia el plan de un usuario.
     * Controla transiciones NONE <-> BASIC/PREMIUM, y entre planes activos.
     */
    @Transactional
    public Subscription subscribeOrChangePlan(UUID userIdValue, String planTypeName) {
        var userId = new UserId(userIdValue);
        var newPlanType = PlanType.valueOf(planTypeName.toUpperCase());

        var existingOpt = subscriptionRepository.findByUserId(userId);

        // Caso 1: Usuario sin suscripción aún → crear nueva
        if (existingOpt.isEmpty()) {
            var plan = planRepository.findByType(newPlanType)
                    .orElseThrow(() -> new IllegalArgumentException("Plan no encontrado: " + newPlanType));

            var newSub = new Subscription(userId, plan);
            if (newPlanType == PlanType.NONE) {
                newSub.cancel(); // queda inactiva si empieza sin plan
            } else {
                newSub.activate();
            }
            return subscriptionRepository.save(newSub);
        }

        // Caso 2: Usuario ya tiene suscripción existente
        var existing = existingOpt.get();
        var currentPlanType = existing.getPlan().getType();

        // Si el usuario pasa a NONE → cancelar la sub
        if (newPlanType == PlanType.NONE) {
            var nonePlan = planRepository.findByType(PlanType.NONE)
                    .orElseThrow(() -> new IllegalArgumentException("Plan NONE no encontrado"));
            existing.cancel();
            existing.setPlan(nonePlan);
            return subscriptionRepository.save(existing);
        }

        // Si el usuario tenía NONE y pasa a un plan pago → activar
        if (currentPlanType == PlanType.NONE && newPlanType != PlanType.NONE) {
            var newPlan = planRepository.findByType(newPlanType)
                    .orElseThrow(() -> new IllegalArgumentException("Plan no encontrado: " + newPlanType));
            existing.setPlan(newPlan);
            existing.activate();
            return subscriptionRepository.save(existing);
        }

        // Si el usuario ya tiene una suscripción activa (BASIC o PREMIUM)
        // y solo está cambiando de plan (manteniendo activa)
        if (existing.isActive()) {
            var newPlan = planRepository.findByType(newPlanType)
                    .orElseThrow(() -> new IllegalArgumentException("Plan no encontrado: " + newPlanType));
            existing.setPlan(newPlan);
            return subscriptionRepository.save(existing);
        }

        // Si estaba cancelada pero no era NONE → reactivar con nuevo plan
        var plan = planRepository.findByType(newPlanType)
                .orElseThrow(() -> new IllegalArgumentException("Plan no encontrado: " + newPlanType));
        existing.setPlan(plan);
        existing.activate();
        return subscriptionRepository.save(existing);
    }

    /**
     * Cancela una suscripción activa.
     */
    @Transactional
    public Subscription cancelSubscription(UUID userIdValue) {
        var userId = new UserId(userIdValue);
        var subscription = subscriptionRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Suscripción no encontrada para este usuario."));

        if (!subscription.isActive()) {
            throw new InvalidSubscriptionStateException("La suscripción no está activa.");
        }

        subscription.cancel();
        return subscriptionRepository.save(subscription);
    }

    /**
     * Reactiva una suscripción cancelada (opcional).
     */
    @Transactional
    public Subscription reactivateSubscription(UUID userIdValue) {
        var userId = new UserId(userIdValue);
        var subscription = subscriptionRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Suscripción no encontrada."));

        if (subscription.getStatus() == SubscriptionStatus.ACTIVE) {
            throw new InvalidSubscriptionStateException("Ya está activa.");
        }

        subscription.activate();
        return subscriptionRepository.save(subscription);
    }
}