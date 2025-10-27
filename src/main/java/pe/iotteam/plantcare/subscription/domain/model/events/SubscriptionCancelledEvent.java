package pe.iotteam.plantcare.subscription.domain.model.events;

import java.util.UUID;

public record SubscriptionCancelledEvent(UUID subscriptionId, UUID customerId) { }
