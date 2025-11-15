package pe.iotteam.plantcare.user.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.iotteam.plantcare.user.application.internal.services.UserProfileService;
import pe.iotteam.plantcare.user.interfaces.rest.resources.*;

@RestController
@RequestMapping("/api/v1/users")
@Tag(
        name = "User Profile",
        description = "Endpoints para gestionar el perfil del usuario autenticado, incluyendo información personal, " +
                "avatar, estadísticas y logros."
)
public class UserProfileController {

    private final UserProfileService service;

    public UserProfileController(UserProfileService service) {
        this.service = service;
    }

    @Operation(
            summary = "Obtener perfil del usuario",
            description = "Obtiene la información completa del perfil del usuario autenticado, " +
                    "incluyendo datos personales, avatar y otras configuraciones."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil obtenido exitosamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "404", description = "Perfil del usuario no encontrado")
    })
    @GetMapping("/profile")
    public ResponseEntity<UserProfileResource> getProfile(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(service.getProfileByEmail(email));
    }

    @Operation(
            summary = "Actualizar perfil del usuario",
            description = "Actualiza la información del perfil del usuario autenticado. " +
                    "Solo se actualizan los campos proporcionados en el request."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o incompletos"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "404", description = "Perfil del usuario no encontrado")
    })
    @PatchMapping("/profile")
    public ResponseEntity<UserProfileResource> updateProfile(Authentication authentication,
                                                             @Valid @RequestBody UpdateProfileRequest request) {
        String email = authentication.getName();
        return ResponseEntity.ok(service.updateProfileByEmail(email, request));
    }

    @Operation(
            summary = "Cargar avatar del usuario",
            description = "Carga o actualiza la imagen de avatar del usuario autenticado. " +
                    "La imagen es almacenada y se retorna su URL pública."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avatar cargado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Archivo inválido o formato no soportado"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "413", description = "Archivo excede el tamaño máximo permitido")
    })
    @PostMapping(value = "/profile/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAvatar(Authentication authentication,
                                          @RequestPart("file") MultipartFile file) throws Exception {
        String email = authentication.getName();
        String url = service.uploadAvatarByEmail(email, file);
        return ResponseEntity.ok().body(java.util.Collections.singletonMap("avatarUrl", url));
    }

    @Operation(
            summary = "Obtener estadísticas del usuario",
            description = "Obtiene las estadísticas de actividad del usuario autenticado, " +
                    "como cantidad de posts, comentarios, plantas registradas, etc."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "404", description = "Estadísticas no encontradas")
    })
    @GetMapping("/stats")
    public ResponseEntity<UserStatsResource> getStats(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(service.getStatsByEmail(email));
    }

    @Operation(
            summary = "Obtener logros del usuario",
            description = "Obtiene la lista de logros (achievements) desbloqueados por el usuario autenticado, " +
                    "incluyendo su estado de progreso y fecha de desbloqueo."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logros obtenidos exitosamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "404", description = "Logros no encontrados")
    })
    @GetMapping("/achievements")
    public ResponseEntity<AchievementsResponse> getAchievements(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(service.getAchievementsByEmail(email));
    }
}
