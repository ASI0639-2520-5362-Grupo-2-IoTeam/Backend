package pe.iotteam.plantcare.community.domain.model.events;

import java.util.UUID;
import java.time.LocalDateTime;
import pe.iotteam.plantcare.community.domain.model.valueobjects.UserId;

public record PostCreatedEvent(UUID postId, UserId authorId, LocalDateTime occurredOn) {}

