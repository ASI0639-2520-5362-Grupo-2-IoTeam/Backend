package pe.iotteam.plantcare.community.interfaces.rest.resources;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostResource(
        Long id,
        String title,
        String content,
        String authorId,
        LocalDateTime createdAt,
        boolean highlighted
) {}