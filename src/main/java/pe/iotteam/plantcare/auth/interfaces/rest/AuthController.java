package pe.iotteam.plantcare.auth.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.iotteam.plantcare.auth.application.internal.commandservices.LoginUserCommandService;
import pe.iotteam.plantcare.auth.application.internal.commandservices.RegisterUserCommandService;
import pe.iotteam.plantcare.auth.domain.model.aggregates.UserAccount;
import pe.iotteam.plantcare.auth.infrastructure.persistence.jpa.repositories.UserRepositoryJpa;
import pe.iotteam.plantcare.auth.interfaces.rest.transform.LoginRequest;
import pe.iotteam.plantcare.auth.interfaces.rest.transform.LoginResponse;
import pe.iotteam.plantcare.auth.interfaces.rest.transform.RegisterRequest;
import pe.iotteam.plantcare.auth.interfaces.rest.transform.UserResponse;
import pe.iotteam.plantcare.subscription.application.internal.commandservices.SubscriptionCommandService;

@RestController
@RequestMapping("/api/v1/authentication")
@Tag(
        name = "Authentication",
        description = "Endpoints responsables del registro, autenticación y generación de tokens para los usuarios."
)
public class AuthController {

    private final RegisterUserCommandService registerService;
    private final LoginUserCommandService loginService;
    private final UserRepositoryJpa userRepository;
    private final SubscriptionCommandService subscriptionService;

    public AuthController(RegisterUserCommandService registerService,
                          LoginUserCommandService loginService, UserRepositoryJpa userRepository,
                          SubscriptionCommandService subscriptionService) {
        this.registerService = registerService;
        this.loginService = loginService;
        this.userRepository = userRepository;
        this.subscriptionService = subscriptionService;
    }
    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Crea un nuevo usuario dentro del sistema utilizando correo, nombre de usuario, contraseña y rol. "
                    + "Después del registro, se genera automáticamente una suscripción inicial con plan 'NONE'."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos enviados en la solicitud"),
            @ApiResponse(responseCode = "409", description = "El email ya está registrado en el sistema")
    })
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        UserAccount user = registerService.handle(
                request.email(),
                request.username(),
                request.password(),
                request.role()
        );

        // Crear suscripción inicial con plan NONE
        subscriptionService.subscribeOrChangePlan(user.getUserId().value(), "NONE");

        return ResponseEntity.ok(new UserResponse(
                user.getUserId().value().toString(),
                user.getEmail().value(),
                user.getUsername(),
                user.getRole()
        ));
    }
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica a un usuario usando email y contraseña, devolviendo un token JWT válido "
                    + "junto con información básica del usuario."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas o usuario no autorizado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = loginService.handle(request.email(), request.password());

        return ResponseEntity.ok(new LoginResponse(
                token,
                user.getUsername(),
                user.getId()
        ));
    }
}