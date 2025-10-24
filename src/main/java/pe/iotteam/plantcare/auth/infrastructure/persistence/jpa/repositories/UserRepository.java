package pe.iotteam.plantcare.auth.infrastructure.persistence.jpa.repositories;


import pe.iotteam.plantcare.auth.domain.model.aggregates.UserAccount;
import pe.iotteam.plantcare.auth.domain.model.valueobjects.Email;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<UserAccount> findByEmail(Email email);
    Optional<UserAccount> findById(UUID userId);
    UserAccount save(UserAccount userAccount);
}