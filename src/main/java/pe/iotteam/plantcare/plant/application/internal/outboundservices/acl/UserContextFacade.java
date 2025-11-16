package pe.iotteam.plantcare.plant.application.internal.outboundservices.acl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.iotteam.plantcare.auth.domain.model.entities.UserEntity;
import pe.iotteam.plantcare.auth.infrastructure.persistence.jpa.repositories.UserRepositoryJpa;

import java.util.Optional;
import java.util.UUID;

/**
 * Anti-Corruption Layer (ACL) Facade for the User context.
 * This component provides a simplified and controlled way for the Plant bounded context
 * to access necessary user data without being tightly coupled to the internal implementation
 * of the Auth context.
 */
@Service
@RequiredArgsConstructor
public final class UserContextFacade {

    private final UserRepositoryJpa userRepository;

    /**
     * Finds a user by their unique identifier.
     *
     * @param userId The UUID of the user to find.
     * @return an {@link Optional} containing the {@link UserEntity} if found, otherwise empty.
     */
    public Optional<UserEntity> findUserById(UUID userId) {
        return userRepository.findById(userId);
    }
}
