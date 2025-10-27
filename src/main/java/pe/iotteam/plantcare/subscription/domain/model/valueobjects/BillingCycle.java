package pe.iotteam.plantcare.subscription.domain.model.valueobjects;

import java.time.LocalDateTime;

public enum BillingCycle {
    MONTHLY,
    YEARLY;

    public LocalDateTime nextBillingDateFrom(LocalDateTime start) {
        return switch (this) {
            case MONTHLY -> start.plusMonths(1);
            case YEARLY -> start.plusYears(1);
        };
    }
}
