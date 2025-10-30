package pe.iotteam.plantcare.auth.interfaces.rest.transform;

import java.util.UUID;

public record LoginResponse(String token, String username, UUID uuid) {}