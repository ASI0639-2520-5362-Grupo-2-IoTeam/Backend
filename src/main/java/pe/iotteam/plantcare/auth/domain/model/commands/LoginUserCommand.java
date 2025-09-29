package pe.iotteam.plantcare.auth.domain.model.commands;

public record LoginUserCommand(String email, String password) {}