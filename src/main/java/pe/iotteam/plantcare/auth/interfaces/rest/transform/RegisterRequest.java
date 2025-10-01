package pe.iotteam.plantcare.auth.interfaces.rest.transform;

import pe.iotteam.plantcare.auth.domain.model.entities.Role;

public record RegisterRequest(String email, String password, Role role) {}