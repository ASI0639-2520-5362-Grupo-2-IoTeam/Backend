package pe.iotteam.plantcare.community.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.iotteam.plantcare.auth.domain.model.entities.Role;
import pe.iotteam.plantcare.community.domain.model.aggregates.CommunityMember;
import pe.iotteam.plantcare.community.domain.model.valueobjects.UserId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommunityMemberRepository extends JpaRepository<CommunityMember, UUID> {

    Optional<CommunityMember> findByUserId(UserId userId);

    List<CommunityMember> findByRole(Role role);

    Optional<CommunityMember> findByUserIdValue(UUID value);

}

