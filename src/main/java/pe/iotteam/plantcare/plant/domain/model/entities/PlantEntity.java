package pe.iotteam.plantcare.plant.domain.model.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "plants")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(nullable = false)
    private String status;

    @Column(length = 1000)
    private String bio;

    private String location;

    private LocalDateTime lastWatered;
    private LocalDateTime nextWatering;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PlantMetricsEntity> metrics = new ArrayList<>();

    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<WateringLogEntity> wateringLogs = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public PlantEntity(String userId, String name, String type, String imgUrl, String status, String bio, String location) {
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.imgUrl = imgUrl;
        this.status = status;
        this.bio = bio;
        this.location = location;
    }

    // Factory para creación explícita manteniendo el constructor protegido por defecto
    public static PlantEntity create() {
        return new PlantEntity();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Helper methods for bidirectional relationship
    public void addMetric(PlantMetricsEntity metric) {
        metrics.add(metric);
        metric.setPlant(this);
    }

    public void removeMetric(PlantMetricsEntity metric) {
        metrics.remove(metric);
        metric.setPlant(null);
    }

    public void addWateringLog(WateringLogEntity wateringLog) {
        wateringLogs.add(wateringLog);
        wateringLog.setPlant(this);
    }

    public void removeWateringLog(WateringLogEntity wateringLog) {
        wateringLogs.remove(wateringLog);
        wateringLog.setPlant(null);
    }
}