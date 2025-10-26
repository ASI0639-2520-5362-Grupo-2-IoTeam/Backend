package pe.iotteam.plantcare.auth.interfaces.rest;

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

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final RegisterUserCommandService registerService;
    private final LoginUserCommandService loginService;
    private final UserRepositoryJpa userRepository;

    public AuthController(RegisterUserCommandService registerService,
                          LoginUserCommandService loginService, UserRepositoryJpa userRepository) {
        this.registerService = registerService;
        this.loginService = loginService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        UserAccount user = registerService.handle(
                request.email(),
                request.username(),
                request.password(),
                request.role()
        );

        return ResponseEntity.ok(new UserResponse(
                user.getUserId().value().toString(),
                user.getEmail().value(),
                user.getUsername(),
                user.getRole()
        ));
    }

    @PostMapping("/login")
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