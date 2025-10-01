package pe.iotteam.plantcare.auth.interfaces.rest.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.iotteam.plantcare.auth.application.internal.commandservices.LoginUserCommandService;
import pe.iotteam.plantcare.auth.application.internal.commandservices.RegisterUserCommandService;
import pe.iotteam.plantcare.auth.domain.model.aggregates.UserAccount;
import pe.iotteam.plantcare.auth.interfaces.rest.transform.LoginRequest;
import pe.iotteam.plantcare.auth.interfaces.rest.transform.LoginResponse;
import pe.iotteam.plantcare.auth.interfaces.rest.transform.RegisterRequest;
import pe.iotteam.plantcare.auth.interfaces.rest.transform.UserResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegisterUserCommandService registerService;
    private final LoginUserCommandService loginService;

    public AuthController(RegisterUserCommandService registerService,
                          LoginUserCommandService loginService) {
        this.registerService = registerService;
        this.loginService = loginService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        UserAccount user = registerService.handle(
                request.email(),
                request.password(),
                request.role()
        );

        return ResponseEntity.ok(new UserResponse(
                user.getUserId().value().toString(),
                user.getEmail().value(),
                user.getRole()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String token = loginService.handle(request.email(), request.password());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}