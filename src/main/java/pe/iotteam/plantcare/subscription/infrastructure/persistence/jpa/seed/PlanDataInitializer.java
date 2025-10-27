package pe.iotteam.plantcare.subscription.infrastructure.persistence.jpa.seed;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.iotteam.plantcare.subscription.domain.model.entities.Plan;
import pe.iotteam.plantcare.subscription.domain.model.valueobjects.BillingCycle;
import pe.iotteam.plantcare.subscription.domain.model.valueobjects.PlanType;
import pe.iotteam.plantcare.subscription.infrastructure.persistence.jpa.repositories.PlanRepository;

import java.math.BigDecimal;

@Component
public class PlanDataInitializer {

    private final PlanRepository planRepository;

    public PlanDataInitializer(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @PostConstruct
    @Transactional
    public void seedPlans() {
        if (planRepository.findByType(PlanType.NONE).isEmpty()) {
            planRepository.save(new Plan(PlanType.NONE, BigDecimal.valueOf(0.00), BillingCycle.MONTHLY));
        }

        if (planRepository.findByType(PlanType.BASIC).isEmpty()) {
            planRepository.save(new Plan(PlanType.BASIC, BigDecimal.valueOf(25.00), BillingCycle.MONTHLY));
        }

        if (planRepository.findByType(PlanType.PREMIUM).isEmpty()) {
            planRepository.save(new Plan(PlanType.PREMIUM, BigDecimal.valueOf(50.00), BillingCycle.MONTHLY));
        }
    }
}
