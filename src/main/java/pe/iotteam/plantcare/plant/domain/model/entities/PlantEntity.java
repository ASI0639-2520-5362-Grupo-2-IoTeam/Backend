package pe.iotteam.plantcare.plant.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.UserId;
import pe.iotteam.plantcare.plant.domain.model.valueobjects.PlantStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "plants")
@Getter
@Setter
@NoArgsConstructor
public class PlantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private UserId userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    private String imgUrl;

    private Double humidity;

    private String lastWatered;
    private String nextWatering;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlantStatus status;

    @Column(length = 1000)
    private String bio;

    private String location;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    private Integer temperature;
    private Integer light;
    private Integer soil_humidity;

    public PlantEntity(UserId userId, String name, String type, String imgUrl,
                       PlantStatus status, String bio, String location) {
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.imgUrl = imgUrl;
        this.status = status;
        this.bio = bio;
        this.location = location;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}