package pe.iotteam.plantcare.plant.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "watering_logs")
@Getter
@Setter
@NoArgsConstructor
public class WateringLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "plant_id", nullable = false)
    private PlantEntity plant;

    private LocalDateTime wateredAt;
}