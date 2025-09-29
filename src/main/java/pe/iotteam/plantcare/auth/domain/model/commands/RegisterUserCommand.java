package pe.iotteam.plantcare.auth.domain.model.commands;

import pe.iotteam.plantcare.auth.domain.model.valueobjects.Email;

public record RegisterUserCommand(String fullName, Email email, String password) {}