package pe.iotteam.plantcare.auth.domain.model.events;

import pe.iotteam.plantcare.auth.domain.model.valueobjects.Email;
import pe.iotteam.plantcare.auth.domain.model.valueobjects.UserId;

public record UserLoggedInEvent(UserId userId, Email email) {}