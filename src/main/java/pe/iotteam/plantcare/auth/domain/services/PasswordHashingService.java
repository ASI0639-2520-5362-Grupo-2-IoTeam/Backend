package pe.iotteam.plantcare.auth.domain.services;

import pe.iotteam.plantcare.auth.domain.model.valueobjects.HashedPassword;

public interface PasswordHashingService {
    HashedPassword hashPassword(String plainPassword);
    boolean verifyPassword(String plainPassword, HashedPassword hashedPassword);
}