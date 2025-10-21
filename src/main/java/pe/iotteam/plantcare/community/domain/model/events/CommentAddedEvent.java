package pe.iotteam.plantcare.community.domain.model.events;

import java.util.UUID;
import java.time.LocalDateTime;
import pe.iotteam.plantcare.community.domain.model.valueobjects.UserId;

public record CommentAddedEvent(UUID commentId, UUID postId, UserId authorId, LocalDateTime occurredOn) {}
