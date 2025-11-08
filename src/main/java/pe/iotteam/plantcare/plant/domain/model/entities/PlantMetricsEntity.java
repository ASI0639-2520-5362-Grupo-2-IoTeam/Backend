package pe.iotteam.plantcare.plant.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "plant_metrics")
@Getter
@Setter
@NoArgsConstructor
public class PlantMetricsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "plant_id", nullable = false)
    private PlantEntity plant;

    private String deviceId;
    private Integer temperature;
    private Integer humidity;
    private Integer light;
    private Integer soilHumidity;
    private LocalDateTime createdAt;
}