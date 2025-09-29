package pe.iotteam.plantcare.auth.domain.model.aggregates;

import pe.iotteam.plantcare.auth.domain.model.entities.Role;
import pe.iotteam.plantcare.auth.domain.model.valueobjects.Email;
import pe.iotteam.plantcare.auth.domain.model.valueobjects.HashedPassword;
import pe.iotteam.plantcare.auth.domain.model.valueobjects.UserId;

import java.time.LocalDateTime;

public class UserAccount {

    private final UserId userId;
    private Email email;
    private HashedPassword password;
    private Role role;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserAccount(UserId userId, Email email, HashedPassword password, Role role) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public UserId getUserId() { return userId; }
    public Email getEmail() { return email; }
    public HashedPassword getPassword() { return password; }
    public Role getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void changePassword(HashedPassword newPassword) {
        this.password = newPassword;
        this.updatedAt = LocalDateTime.now();
    }

    public void changeRole(Role newRole) {
        this.role = newRole;
        this.updatedAt = LocalDateTime.now();
    }
}