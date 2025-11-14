package pe.iotteam.plantcare.community.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Community Members",
        description = "Gestión de miembros dentro de la comunidad, incluyendo registro y obtención del listado de miembros."
)
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
    @Operation(
            summary = "Registrar un nuevo miembro de la comunidad",
            description = "Crea un nuevo registro de miembro para el usuario especificado. "
                    + "El rol asignado por defecto será el del usuario en el sistema (usualmente USER)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Miembro registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida o usuario no encontrado")
    })
    @PostMapping
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
    @Operation(
            summary = "Listar todos los miembros registrados",
            description = "Devuelve la lista completa de miembros pertenecientes a la comunidad."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de miembros obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<CommunityMemberResource>> listMembers() {
        var members = memberRepository.findAll()
                .stream()
                .map(CommunityMemberResourceAssembler::toResource)
                .toList();
        return ResponseEntity.ok(members);
    }
}