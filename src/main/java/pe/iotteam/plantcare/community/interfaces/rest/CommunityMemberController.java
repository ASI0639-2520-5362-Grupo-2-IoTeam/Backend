package pe.iotteam.plantcare.community.interfaces.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.iotteam.plantcare.auth.domain.model.aggregates.UserAccount;
import pe.iotteam.plantcare.auth.domain.model.entities.Role;
import pe.iotteam.plantcare.auth.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.iotteam.plantcare.community.application.internal.commandservices.CommunityMembershipCommandService;
import pe.iotteam.plantcare.community.domain.model.aggregates.CommunityMember;
import pe.iotteam.plantcare.community.infrastructure.persistence.jpa.repositories.CommunityMemberRepository;
import pe.iotteam.plantcare.community.interfaces.rest.transform.CommunityMemberResourceAssembler;
import pe.iotteam.plantcare.community.interfaces.rest.resources.CommunityMemberResource;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/community/members")
public class CommunityMemberController {

    private final CommunityMembershipCommandService membershipCommandService;
    private final CommunityMemberRepository memberRepository;
    private final UserRepository userRepository;

    public CommunityMemberController(
            CommunityMembershipCommandService membershipCommandService,
            CommunityMemberRepository memberRepository, UserRepository userRepository) {
        this.membershipCommandService = membershipCommandService;
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
    }

    /**
     * Registra un nuevo miembro con rol USER por defecto
     */
    @PostMapping("/register")
    public ResponseEntity<CommunityMemberResource> registerMember(
            @RequestParam UUID userId
    ) {
        // obtener rol del usuario usando el UserRepository
        Role role = userRepository.findById(userId)
                .map(UserAccount::getRole)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        CommunityMember member = membershipCommandService.registerMember(userId, role);
        return ResponseEntity.ok(CommunityMemberResourceAssembler.toResource(member));
    }


    //Cuando se añadan moderadores como rol opcion. Para el TF
    /**
     * Promueve un miembro a ADMIN

    @PostMapping("/{userId}/promote")
    public ResponseEntity<CommunityMemberResource> promoteToAdmin(@PathVariable UUID userId) {
        CommunityMember member = membershipCommandService.promoteToAdmin(userId);
        return ResponseEntity.ok(CommunityMemberResourceAssembler.toResource(member));
    }*/

    /**
     * Degrada un miembro a USER
     *
    @PostMapping("/{userId}/demote")
    public ResponseEntity<CommunityMemberResource> demoteToUser(@PathVariable UUID userId) {
        CommunityMember member = membershipCommandService.demoteToUser(userId);
        return ResponseEntity.ok(CommunityMemberResourceAssembler.toResource(member));
    }*/

    /**
     * Lista todos los miembros registrados
     */
    @GetMapping
    public ResponseEntity<List<CommunityMemberResource>> listMembers() {
        var members = memberRepository.findAll()
                .stream()
                .map(CommunityMemberResourceAssembler::toResource)
                .toList();
        return ResponseEntity.ok(members);
    }
}
