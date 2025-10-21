package pe.iotteam.plantcare.community.domain.model.events;

import pe.iotteam.plantcare.community.domain.model.valueobjects.UserId;
import java.time.LocalDateTime;

public record MemberPromotedEvent(UserId userId, LocalDateTime occurredOn) {}