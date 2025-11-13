package pe.iotteam.plantcare.user.interfaces.rest.resources;

import java.util.List;

public record AchievementsResponse(
        List<AchievementResource> achievements
) {}
