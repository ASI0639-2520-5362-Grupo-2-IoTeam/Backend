package pe.iotteam.plantcare.auth.domain.model.commands;

import pe.iotteam.plantcare.auth.domain.model.valueobjects.Email;

public record LoginUserCommand(Email email, String password) {}