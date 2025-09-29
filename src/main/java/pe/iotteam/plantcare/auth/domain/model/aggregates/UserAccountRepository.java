package pe.iotteam.plantcare.auth.domain.model.aggregates;

import java.util.Optional;

public interface UserAccountRepository {
    Optional<UserAccount> findByEmail(String email);
    UserAccount save(UserAccount user);
}