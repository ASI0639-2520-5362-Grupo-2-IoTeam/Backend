package pe.iotteam.plantcare.subscription.domain.model.entities;

import jakarta.persistence.*;
import pe.iotteam.plantcare.subscription.domain.model.valueobjects.BillingCycle;
import pe.iotteam.plantcare.subscription.domain.model.valueobjects.PlanType;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "plans")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private PlanType type; // BASIC o PREMIUM o NONE

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillingCycle billingCycle;

    protected Plan() {}

    public Plan(PlanType type, BigDecimal price, BillingCycle billingCycle) {
        this.type = type;
        this.price = price;
        this.billingCycle = billingCycle;
    }

    // Getters
    public UUID getId() { return id; }
    public PlanType getType() { return type; }
    public BigDecimal getPrice() { return price; }
    public BillingCycle getBillingCycle() { return billingCycle; }

    public boolean isFree() {
        return price.compareTo(BigDecimal.ZERO) == 0;
    }
}
