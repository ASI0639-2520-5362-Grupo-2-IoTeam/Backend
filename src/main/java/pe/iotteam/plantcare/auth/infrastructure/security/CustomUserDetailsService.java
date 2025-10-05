package pe.iotteam.plantcare.auth.infrastructure.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.iotteam.plantcare.auth.domain.model.aggregates.UserAccount;
import pe.iotteam.plantcare.auth.domain.model.valueobjects.Email;
import pe.iotteam.plantcare.auth.domain.model.valueobjects.HashedPassword;
import pe.iotteam.plantcare.auth.domain.model.valueobjects.UserId;
import pe.iotteam.plantcare.auth.infrastructure.persistence.jpa.repositories.UserRepositoryJpa;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepositoryJpa userRepository;

    public CustomUserDetailsService(UserRepositoryJpa userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(entity -> new UserAccount(
                        new UserId(entity.getId()),
                        new Email(entity.getEmail()),
                        entity.getUsername(),
                        new HashedPassword(entity.getPassword()),
                        entity.getRole()
                ))
                .map(account -> org.springframework.security.core.userdetails.User
                        .withUsername(String.valueOf(account.getEmail().value()))
                        .password(account.getHashedPassword().value())
                        .roles(account.getRole().name())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}