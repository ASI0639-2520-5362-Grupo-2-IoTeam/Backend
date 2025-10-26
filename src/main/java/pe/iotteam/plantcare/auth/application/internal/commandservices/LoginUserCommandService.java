package pe.iotteam.plantcare.auth.application.internal.commandservices;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.iotteam.plantcare.auth.domain.model.aggregates.UserAccount;
import pe.iotteam.plantcare.auth.domain.model.entities.Role;
import pe.iotteam.plantcare.auth.domain.model.valueobjects.Email;
import pe.iotteam.plantcare.auth.domain.model.valueobjects.HashedPassword;
import pe.iotteam.plantcare.auth.domain.model.valueobjects.UserId;
import pe.iotteam.plantcare.auth.infrastructure.oauth.google.services.GoogleOAuthService;
import pe.iotteam.plantcare.auth.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.iotteam.plantcare.auth.infrastructure.persistence.jpa.repositories.UserRepositoryJpa;
import pe.iotteam.plantcare.auth.infrastructure.security.JwtTokenProvider;

import java.util.UUID;

@Service
public class LoginUserCommandService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final GoogleOAuthService googleOAuthService;

    public LoginUserCommandService(UserRepository userRepository,
                                   PasswordEncoder passwordEncoder,
                                   JwtTokenProvider jwtTokenProvider,
                                   GoogleOAuthService googleOAuthService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.googleOAuthService = googleOAuthService;
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

    @Transactional
    public String handleGoogleSignIn(String googleIdToken) {
        try {
            var payload = googleOAuthService.verifyToken(googleIdToken);
            String emailStr = payload.getEmail();
            // String name = (String) payload.get("name"); // tu aggregate no tiene campo nombre actualmente

            if (emailStr == null || emailStr.isBlank()) {
                throw new RuntimeException("El token de Google no contiene email");
            }

            Email email = new Email(emailStr);
            var existing = userRepository.findByEmail(email);

            UserAccount user;
            if (existing.isPresent()) {
                user = existing.get();
            } else {
                Role defaultRole = Role.USER;

                UserId newUserId = new UserId(UUID.randomUUID());

                String randomPassword = UUID.randomUUID().toString();
                HashedPassword hashedPassword = new HashedPassword(passwordEncoder.encode(randomPassword));

                String username = emailStr;

                user = new UserAccount(
                        newUserId,
                        email,
                        username,
                        hashedPassword,
                        defaultRole
                );

                userRepository.save(user);
            }

            return jwtTokenProvider.createToken(
                    user.getEmail().value(),
                    user.getRole().name()
            );

        } catch (Exception e) {
            throw new RuntimeException("Google authentication failed: " + e.getMessage(), e);
        }
    }

    @Transactional
    public String handleGoogleCallback(Object command) {
        throw new UnsupportedOperationException("Google callback not implemented yet");
    }


}