package pe.iotteam.plantcare.auth.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.iotteam.plantcare.auth.domain.model.entities.UserEntity;

import java.util.Optional;

public interface UserRepositoryJpa extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}