package pe.iotteam.plantcare.plant.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "plants")
@Getter
@Setter
@NoArgsConstructor
public class PlantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    private String imgUrl;

    private LocalDateTime lastWatered;
    private LocalDateTime nextWatering;

    @Column(nullable = false)
    private String status;

    @Column(length = 1000)
    private String bio;

    private String location;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public PlantEntity(String userId, String name, String type, String imgUrl,
                       String status, String bio, String location) {
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