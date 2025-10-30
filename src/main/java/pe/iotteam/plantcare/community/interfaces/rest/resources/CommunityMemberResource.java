package pe.iotteam.plantcare.community.interfaces.rest.resources;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommunityMemberResource(
        UUID userId,
        String role,
        LocalDateTime joinedAt
) {}