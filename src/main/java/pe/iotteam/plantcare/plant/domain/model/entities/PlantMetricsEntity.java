package pe.iotteam.plantcare.plant.domain.model.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "plant_metrics")
@Getter
@Setter // Setters are kept for JPA and mapping libraries' convenience.
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlantMetricsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id", nullable = false)
    private PlantEntity plant;

    private String deviceId;
    private Integer temperature;
    private Integer humidity;
    private Integer light;
    private Integer soilHumidity;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public PlantMetricsEntity(String deviceId, Integer temperature, Integer humidity, Integer light, Integer soilHumidity) {
        this.deviceId = deviceId;
        this.temperature = temperature;
        this.humidity = humidity;
        this.light = light;
        this.soilHumidity = soilHumidity;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}