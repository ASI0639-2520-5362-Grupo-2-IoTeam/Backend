package pe.iotteam.plantcare.auth.domain.model.queries;

import pe.iotteam.plantcare.auth.domain.model.valueobjects.UserId;

public record GetUserByIdQuery(UserId userId) {}