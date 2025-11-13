package pe.iotteam.plantcare.analytics.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * JPA Entity for sensor data records
 */
@Entity
@Table(name = "sensor_data_analytics", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"device_id", "created_at"}))
@Getter
@Setter
@NoArgsConstructor
public class SensorDataEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(name = "temperature")
    private Integer temperature;

    @Column(name = "humidity")
    private Integer humidity;

    @Column(name = "light")
    private Integer light;

    @Column(name = "soil_humidity")
    private Integer soilHumidity;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "ingested_at", nullable = false)
    private LocalDateTime ingestedAt;

    public SensorDataEntity(String deviceId, Integer temperature, Integer humidity, 
                           Integer light, Integer soilHumidity, LocalDateTime createdAt) {
        this.deviceId = deviceId;
        this.temperature = temperature;
        this.humidity = humidity;
        this.light = light;
        this.soilHumidity = soilHumidity;
        this.createdAt = createdAt;
        this.ingestedAt = LocalDateTime.now();
    }
}
