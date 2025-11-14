package pe.iotteam.plantcare.auth.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.iotteam.plantcare.auth.application.internal.commandservices.LoginUserCommandService;
import pe.iotteam.plantcare.auth.domain.model.valueobjects.Email;
import pe.iotteam.plantcare.auth.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.iotteam.plantcare.auth.infrastructure.security.JwtTokenProvider;
import pe.iotteam.plantcare.auth.interfaces.rest.resources.AuthResponse;
import pe.iotteam.plantcare.auth.interfaces.rest.resources.GoogleSignInResource;
import pe.iotteam.plantcare.auth.interfaces.rest.resources.UserDto;
import pe.iotteam.plantcare.auth.interfaces.rest.transform.GoogleSignInCommandFromResourceAssembler;

@RestController
@RequestMapping(value = "/api/v1/authentication/google")
@Tag(name = "Google Authentication", description = "Google Authentication Endpoints")
public class GoogleAuthController {
    private final LoginUserCommandService loginUserCommandService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public GoogleAuthController(LoginUserCommandService loginUserCommandService,
                                JwtTokenProvider jwtTokenProvider,
                                UserRepository userRepository) {
        this.loginUserCommandService = loginUserCommandService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @PostMapping("/signin")
    @Operation(
            summary = "Iniciar sesión con Google",
            description = "Autentica mediante OAuth de Google."
    )
    public ResponseEntity<?> signInWithGoogle(@Valid @RequestBody GoogleSignInResource resource) {
        try {
            // convertir resource -> command
            var command = GoogleSignInCommandFromResourceAssembler.toCommandFromResource(resource);

            // llamar al servicio (usa el token dentro del command)
            String jwt = loginUserCommandService.handleGoogleSignIn(command.googleToken());

            // recuperar email desde el token para buscar el usuario
            String email = jwtTokenProvider.getUsername(jwt);
            var userOpt = userRepository.findByEmail(new Email(email));
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("User created but not found after creation");
            }

            var user = userOpt.get();
            String id = user.getUserId() != null ? user.getUserId().toString() : null;

            UserDto userDto = new UserDto(id, user.getEmail().value(), user.getUsername(), user.getRole().name());
            AuthResponse response = new AuthResponse(jwt, "Bearer", userDto);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Authentication failed: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Bad request: " + e.getMessage());
        }
    }
}