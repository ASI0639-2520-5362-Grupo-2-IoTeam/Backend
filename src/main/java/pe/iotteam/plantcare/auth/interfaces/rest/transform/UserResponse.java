package pe.iotteam.plantcare.auth.interfaces.rest.transform;

import pe.iotteam.plantcare.auth.domain.model.entities.Role;

public record UserResponse(String id, String email,String username, Role role) {}