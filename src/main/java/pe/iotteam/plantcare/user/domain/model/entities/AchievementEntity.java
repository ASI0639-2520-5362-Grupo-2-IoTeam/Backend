package pe.iotteam.plantcare.user.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "achievements")
@Getter
@Setter
@NoArgsConstructor
public class AchievementEntity {

    @Id
    private String id; // e.g. "first-plant"

    private String userId;

    private String title;

    @Column(length = 1000)
    private String description;

    private String icon;

    private LocalDateTime earnedDate;

    @Enumerated(EnumType.STRING)
    private AchievementStatus status = AchievementStatus.LOCKED;
}
