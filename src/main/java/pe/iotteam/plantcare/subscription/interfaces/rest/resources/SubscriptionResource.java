package pe.iotteam.plantcare.subscription.interfaces.rest.resources;

import pe.iotteam.plantcare.subscription.domain.model.valueobjects.SubscriptionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record SubscriptionResource(
        UUID id,
        UUID userId,
        String planName,
        SubscriptionStatus status,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime nextBillingDate
) {}