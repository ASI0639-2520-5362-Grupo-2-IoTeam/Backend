package pe.iotteam.plantcare.subscription.domain.model.events;

import java.util.UUID;

public record SubscriptionCreatedEvent(UUID subscriptionId, UUID customerId, UUID planId) { }
