package pe.iotteam.plantcare.auth.infrastructure.persistence.jpa.repositories;


import pe.iotteam.plantcare.auth.domain.model.aggregates.UserAccount;
import pe.iotteam.plantcare.auth.domain.model.valueobjects.Email;

import java.util.Optional;

public interface UserRepository {
    Optional<UserAccount> findByEmail(Email email);
    UserAccount save(UserAccount userAccount);
}