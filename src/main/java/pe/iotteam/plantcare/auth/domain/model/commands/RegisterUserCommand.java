package pe.iotteam.plantcare.auth.domain.model.commands;

public record RegisterUserCommand(String fullName, String email, String password) {}