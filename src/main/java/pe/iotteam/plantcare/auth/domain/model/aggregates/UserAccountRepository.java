package pe.iotteam.plantcare.auth.domain.model.aggregates;

import pe.iotteam.plantcare.auth.domain.model.entities.UserAccount;

import java.util.Optional;

public interface UserAccountRepository {
    Optional<UserAccount> findByEmail(String email);
    UserAccount save(UserAccount user);
}