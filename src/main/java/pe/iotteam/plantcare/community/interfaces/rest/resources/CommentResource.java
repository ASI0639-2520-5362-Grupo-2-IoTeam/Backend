package pe.iotteam.plantcare.community.interfaces.rest.resources;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResource(
        UUID id,
        String postId,
        String content,
        LocalDateTime createdAt
) {}