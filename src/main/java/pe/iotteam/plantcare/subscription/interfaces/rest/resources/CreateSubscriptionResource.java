package pe.iotteam.plantcare.subscription.interfaces.rest.resources;

import java.util.UUID;

public record CreateSubscriptionResource(
        UUID userId,
        String planType // BASIC o PREMIUM
) {}
