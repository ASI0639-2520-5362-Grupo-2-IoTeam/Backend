package pe.iotteam.plantcare.auth.domain.services;

import pe.iotteam.plantcare.auth.domain.model.aggregates.UserAccount;
import pe.iotteam.plantcare.auth.domain.model.valueobjects.JwtToken;

public interface JwtService {
    JwtToken generateToken(UserAccount user);
    boolean validateToken(String token);
}