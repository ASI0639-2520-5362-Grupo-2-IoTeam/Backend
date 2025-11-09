package pe.iotteam.plantcare.user.interfaces.rest.resources;

import java.time.LocalDateTime;

public record AchievementResource(
        String id,
        String title,
        String description,
        String icon,
        LocalDateTime earnedDate,
        String status
) {}
