package pe.iotteam.plantcare.auth.domain.model.queries;

import pe.iotteam.plantcare.auth.domain.model.valueobjects.Email;

public record GetUserByEmailQuery(Email email) {}