package pe.iotteam.plantcare.user.interfaces.rest.resources;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserProfileResource(
        UUID uuid,
        String email,
        String username,
        String fullName,
        String phone,
        String bio,
        String location,
        String avatarUrl,
        LocalDateTime joinDate,
        LocalDateTime lastLogin,
        UserStatsResource stats
) {
}
