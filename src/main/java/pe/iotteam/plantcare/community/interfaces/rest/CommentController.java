package pe.iotteam.plantcare.community.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.iotteam.plantcare.community.application.internal.commandservices.CommunityContentCommandService;
import pe.iotteam.plantcare.community.domain.model.entities.Comment;

import java.util.UUID;

@RestController
@RequestMapping("/api/community/comments")
@Tag(
        name = "Community Comments",
        description = "Endpoints para gestionar comentarios realizados por los usuarios dentro de la comunidad."
)
public class CommentController {

    private final CommunityContentCommandService contentCommandService;

    public CommentController(CommunityContentCommandService contentCommandService) {
        this.contentCommandService = contentCommandService;
    }

    @Operation(
            summary = "Agregar un comentario a un post",
            description = "Permite que un usuario registrado agregue un comentario a un post existente dentro de la comunidad. "
                    + "El comentario queda asociado tanto al usuario como al post indicado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentario agregado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida o parámetros faltantes"),
            @ApiResponse(responseCode = "404", description = "El usuario o el post no existen"),
            @ApiResponse(responseCode = "403", description = "El usuario no tiene permiso para comentar")
    })
    @PostMapping
    public ResponseEntity<Comment> addComment(
            @RequestParam UUID userId,
            @RequestParam Long postId,
            @RequestParam String text
    ) {
        Comment comment = contentCommandService.addComment(userId, postId, text);
        return ResponseEntity.ok(comment);
    }
}