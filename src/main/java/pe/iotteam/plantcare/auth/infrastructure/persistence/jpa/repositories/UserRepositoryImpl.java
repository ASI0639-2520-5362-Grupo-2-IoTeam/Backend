package pe.iotteam.plantcare.auth.infrastructure.persistence.jpa.repositories;
import org.springframework.stereotype.Repository;
import pe.iotteam.plantcare.auth.domain.model.aggregates.UserAccount;
import pe.iotteam.plantcare.auth.domain.model.valueobjects.Email;
import pe.iotteam.plantcare.auth.infrastructure.persistence.jpa.mappers.UserMapper;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserRepositoryJpa jpaRepository;

    public UserRepositoryImpl(UserRepositoryJpa jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<UserAccount> findById(UUID userId) {
        return jpaRepository.findById(userId)
                .map(UserMapper::toAggregate);
    }

    @Override
    public Optional<UserAccount> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.value())
                .map(UserMapper::toAggregate);
    }

    @Override
    public UserAccount save(UserAccount userAccount) {
        var entity = UserMapper.toEntity(userAccount);
        var saved = jpaRepository.save(entity);
        return UserMapper.toAggregate(saved);
    }
}