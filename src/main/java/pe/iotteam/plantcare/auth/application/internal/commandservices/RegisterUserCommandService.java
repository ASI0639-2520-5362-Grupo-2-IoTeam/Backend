package pe.iotteam.plantcare.auth.application.internal.commandservices;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.iotteam.plantcare.auth.domain.model.aggregates.UserAccount;
import pe.iotteam.plantcare.auth.domain.model.entities.Role;
import pe.iotteam.plantcare.auth.domain.model.entities.UserEntity;
import pe.iotteam.plantcare.auth.domain.model.valueobjects.Email;
import pe.iotteam.plantcare.auth.domain.model.valueobjects.HashedPassword;
import pe.iotteam.plantcare.auth.domain.model.valueobjects.UserId;
import pe.iotteam.plantcare.auth.infrastructure.persistence.jpa.mappers.UserMapper;
import pe.iotteam.plantcare.auth.infrastructure.persistence.jpa.repositories.UserRepositoryJpa;

import java.util.UUID;

@Service
public class RegisterUserCommandService {

    private final UserRepositoryJpa userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserCommandService(UserRepositoryJpa userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserAccount handle(String email, String rawPassword, Role role) {
        String encoded = passwordEncoder.encode(rawPassword);
        HashedPassword hashed = new HashedPassword(encoded);

        UserAccount newUser = new UserAccount(
                new UserId(UUID.randomUUID()),
                new Email(email),
                hashed,
                role
        );

        UserEntity entity = UserMapper.toEntity(newUser);
        UserEntity savedEntity = userRepository.save(entity);

        return UserMapper.toAggregate(savedEntity);
    }

}
