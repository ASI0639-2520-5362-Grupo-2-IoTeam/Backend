package pe.iotteam.plantcare.subscription.domain.model.aggregates;

import jakarta.persistence.*;
import pe.iotteam.plantcare.subscription.domain.exceptions.InvalidSubscriptionStateException;
import pe.iotteam.plantcare.subscription.domain.model.entities.Plan;
import pe.iotteam.plantcare.subscription.domain.model.valueobjects.SubscriptionStatus;
import pe.iotteam.plantcare.subscription.domain.model.valueobjects.UserId;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "user_id", nullable = false, unique = true))
    })
    private UserId userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime nextBillingDate;

    protected Subscription() {}

    public Subscription(UserId userId, Plan plan) {
        this.userId = userId;
        this.plan = plan;
        this.status = SubscriptionStatus.PENDING;
        this.startDate = LocalDateTime.now();
        if (plan != null) {
            this.nextBillingDate = plan.getBillingCycle().nextBillingDateFrom(this.startDate);
        }
    }
    public void setPlan(Plan newPlan) {
        if (newPlan == null) {
            throw new InvalidSubscriptionStateException("El plan no puede ser nulo.");
        }
        this.plan = newPlan;
        this.nextBillingDate = newPlan.getBillingCycle().nextBillingDateFrom(LocalDateTime.now());
    }

    public void activate() {
        if (this.status == SubscriptionStatus.ACTIVE)
            throw new InvalidSubscriptionStateException("La suscripción ya está activa.");
        this.status = SubscriptionStatus.ACTIVE;
        this.nextBillingDate = LocalDateTime.now().plusMonths(1);
    }

    public void cancel() {
        if (this.status == SubscriptionStatus.CANCELLED)
            throw new InvalidSubscriptionStateException("La suscripción ya está cancelada.");
        this.status = SubscriptionStatus.CANCELLED;
        this.endDate = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public UserId getUserId() { return userId; }
    public Plan getPlan() { return plan; }
    public SubscriptionStatus getStatus() { return status; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public LocalDateTime getNextBillingDate() { return nextBillingDate; }

    public boolean isActive() {
        return this.status == SubscriptionStatus.ACTIVE;
    }

    public boolean isPremium() {
        return this.plan != null && this.plan.getType().name().equals("PREMIUM");
    }
}