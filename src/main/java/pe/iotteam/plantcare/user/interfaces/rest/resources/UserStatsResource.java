package pe.iotteam.plantcare.user.interfaces.rest.resources;

public record UserStatsResource(
        long totalPlants,
        long wateringSessions,
        int successRate
) {
}
