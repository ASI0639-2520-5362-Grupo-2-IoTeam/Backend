package pe.iotteam.plantcare.subscription.domain.model.events;

import java.util.UUID;

public record SubscriptionActivatedEvent(UUID subscriptionId, UUID customerId) { }
