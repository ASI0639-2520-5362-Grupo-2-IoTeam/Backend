package pe.iotteam.plantcare.community.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.iotteam.plantcare.community.application.internal.commandservices.CommunityContentCommandService;
import pe.iotteam.plantcare.community.application.internal.queryservices.CommunityFeedQueryService;
import pe.iotteam.plantcare.community.domain.model.entities.Post;
import pe.iotteam.plantcare.community.interfaces.rest.resources.PostResource;
import pe.iotteam.plantcare.community.interfaces.rest.transform.PostResourceAssembler;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/community/posts")
@Tag(
        name = "Community Posts",
        description = "Endpoints para la gestión de publicaciones dentro de la comunidad de PlantCare"
)
public class PostController {

    private final CommunityContentCommandService contentCommandService;
    private final CommunityFeedQueryService feedQueryService;

    public PostController(
            CommunityContentCommandService contentCommandService,
            CommunityFeedQueryService feedQueryService) {
        this.contentCommandService = contentCommandService;
        this.feedQueryService = feedQueryService;
    }
    @Operation(
            summary = "Crear una nueva publicación",
            description = "Permite que un usuario registrado cree una publicación en la comunidad. "
                    + "La publicación incluye título, contenido, especie de planta asociada y tag."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Publicación creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PostMapping
    public ResponseEntity<PostResource> createPost(
            @RequestParam UUID userId,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String species,
            @RequestParam String tag
    ) {
        Post post = contentCommandService.createPost(userId, title, content, species, tag);
        return ResponseEntity.ok(PostResourceAssembler.toResource(post));
    }
    @Operation(
            summary = "Obtener todas las publicaciones",
            description = "Devuelve una lista completa de publicaciones ordenadas por fecha de creación, "
                    + "incluyendo información del autor, contenido, especie y tag."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de publicaciones obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<PostResource>> getAllPosts() {
        var posts = feedQueryService.getAllPosts()
                .stream()
                .map(PostResourceAssembler::toResource)
                .toList();
        return ResponseEntity.ok(posts);
    }
    @Operation(
            summary = "Eliminar una publicación propia",
            description = "Permite que un usuario elimine una publicación que le pertenece. "
                    + "El sistema valida que el usuario sea el propietario antes de eliminarla."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Publicación eliminada correctamente"),
            @ApiResponse(responseCode = "403", description = "El usuario no es propietario de la publicación"),
            @ApiResponse(responseCode = "404", description = "Publicación o usuario no encontrado")
    })
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deleteOwnPost(@RequestParam UUID userId, @PathVariable Long postId) {
        contentCommandService.deleteOwnPost(userId, postId);
        return ResponseEntity.noContent().build();
    }
}