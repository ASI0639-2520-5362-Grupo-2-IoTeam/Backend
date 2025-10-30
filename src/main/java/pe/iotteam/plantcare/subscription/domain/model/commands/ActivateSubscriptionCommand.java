package pe.iotteam.plantcare.subscription.domain.model.commands;

import java.util.UUID;

public record ActivateSubscriptionCommand(UUID subscriptionId) { }