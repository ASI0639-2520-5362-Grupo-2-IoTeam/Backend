package pe.iotteam.plantcare.subscription.domain.model.commands;

import java.util.UUID;

public record CreateSubscriptionCommand(UUID customerId, UUID planId) { }
