package pe.iotteam.plantcare.user.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.iotteam.plantcare.user.domain.model.entities.AchievementEntity;

import java.util.List;

@Repository
public interface AchievementRepository extends JpaRepository<AchievementEntity, String> {
    List<AchievementEntity> findByUserId(String userId);
}
