package pe.iotteam.plantcare.subscription.domain.model.queries;

import java.util.UUID;

public record GetSubscriptionsByCustomerQuery(UUID customerId) { }
