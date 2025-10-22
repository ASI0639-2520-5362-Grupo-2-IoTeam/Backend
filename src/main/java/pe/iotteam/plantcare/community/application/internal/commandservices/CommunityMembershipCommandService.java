package pe.iotteam.plantcare.community.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.iotteam.plantcare.auth.domain.model.entities.Role;
import pe.iotteam.plantcare.community.domain.model.aggregates.CommunityMember;
import pe.iotteam.plantcare.community.domain.model.valueobjects.UserId;
import pe.iotteam.plantcare.community.infrastructure.persistence.jpa.repositories.CommunityMemberRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class CommunityMembershipCommandService {

    private final CommunityMemberRepository communityMemberRepository;

    public CommunityMembershipCommandService(CommunityMemberRepository communityMemberRepository) {
        this.communityMemberRepository = communityMemberRepository;
    }

    /**
     * Registra un nuevo miembro en la comunidad con rol por defecto USER.
     */
    @Transactional
    public CommunityMember registerMember(UUID userIdValue) {
        UserId userId = new UserId(userIdValue);

        // Evitar duplicados
        Optional<CommunityMember> existing = communityMemberRepository.findByUserIdValue(userIdValue);
        if (existing.isPresent()) {
            throw new IllegalStateException("El usuario ya es miembro de la comunidad.");
        }

        CommunityMember newMember = new CommunityMember(userId, Role.USER);
        return communityMemberRepository.save(newMember);
    }

    /**
     * Promueve a un miembro existente a ADMIN.
     */
    @Transactional
    public CommunityMember promoteToAdmin(UUID userIdValue) {
        CommunityMember member = communityMemberRepository.findByUserIdValue(userIdValue)
                .orElseThrow(() -> new IllegalArgumentException("Miembro no encontrado."));

        member.promoteToAdmin();
        return communityMemberRepository.save(member);
    }

    /**
     * Degrada a un miembro a USER.
     */
    @Transactional
    public CommunityMember demoteToUser(UUID userIdValue) {
        CommunityMember member = communityMemberRepository.findByUserIdValue(userIdValue)
                .orElseThrow(() -> new IllegalArgumentException("Miembro no encontrado."));

        member.demoteToMember();
        return communityMemberRepository.save(member);
    }
}