package pe.iotteam.plantcare.auth.application.internal.queryservices;

import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import pe.iotteam.plantcare.auth.domain.model.entities.UserEntity;
import pe.iotteam.plantcare.auth.domain.model.valueobjects.Email;
import pe.iotteam.plantcare.auth.infrastructure.persistence.jpa.repositories.UserRepositoryJpa;

import java.util.Optional;

@Service
public class UserQueryService {

    private final UserRepositoryJpa userRepository;

    public UserQueryService(UserRepositoryJpa userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserEntity> getUserByEmail(Email email) {
        return userRepository.findByEmail(email.value());
    }
}