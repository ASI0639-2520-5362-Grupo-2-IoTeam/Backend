package pe.iotteam.plantcare.auth.application.internal.commandservices;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.iotteam.plantcare.auth.domain.model.aggregates.UserAccount;
import pe.iotteam.plantcare.auth.domain.model.valueobjects.Email;
import pe.iotteam.plantcare.auth.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.iotteam.plantcare.auth.infrastructure.persistence.jpa.repositories.UserRepositoryJpa;
import pe.iotteam.plantcare.auth.infrastructure.security.JwtTokenProvider;

@Service
public class LoginUserCommandService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginUserCommandService(UserRepository userRepository,
                                   PasswordEncoder passwordEncoder,
                                   JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional(readOnly = true)
    public String handle(String email, String rawPassword) {
        UserAccount user = userRepository.findByEmail(new Email(email))
                .orElseThrow();
        if (!passwordEncoder.matches(rawPassword, user.getHashedPassword().value())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        // Generar el token JWT
        return jwtTokenProvider.createToken(
                user.getEmail().value(),
                user.getRole().name()
        );
    }
}