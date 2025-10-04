package pe.iotteam.plantcare.plant.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.iotteam.plantcare.auth.infrastructure.persistence.jpa.repositories.UserRepositoryJpa;
import pe.iotteam.plantcare.auth.domain.model.entities.UserEntity;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserContextFacade {

    private final UserRepositoryJpa userRepository;

    public UserContextFacade(UserRepositoryJpa userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserEntity> getUserById(UUID userId) {
        return userRepository.findById(userId);
    }
}