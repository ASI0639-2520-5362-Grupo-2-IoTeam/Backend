package pe.iotteam.plantcare.analytics.infrastructure.persistence.jpa.repositories;

import org.springframework.stereotype.Repository;
import pe.iotteam.plantcare.analytics.domain.model.aggregates.SensorDataRecord;
import pe.iotteam.plantcare.analytics.infrastructure.persistence.jpa.mappers.SensorDataMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of SensorDataRepository using Spring Data JPA
 */
@Repository
public class SensorDataRepositoryImpl implements SensorDataRepository {

    private final SensorDataJpaRepository jpaRepository;

    public SensorDataRepositoryImpl(SensorDataJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public SensorDataRecord save(SensorDataRecord record) {
        var entity = SensorDataMapper.toEntity(record);
        var savedEntity = jpaRepository.save(entity);
        return SensorDataMapper.toDomain(savedEntity);
    }

    @Override
    public List<SensorDataRecord> saveAll(List<SensorDataRecord> records) {
        var entities = records.stream()
                .map(SensorDataMapper::toEntity)
                .collect(Collectors.toList());
        var savedEntities = jpaRepository.saveAll(entities);
        return savedEntities.stream()
                .map(SensorDataMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<SensorDataRecord> findAll() {
        return jpaRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(SensorDataMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<SensorDataRecord> findByDeviceId(String deviceId) {
        return jpaRepository.findByDeviceId(deviceId).stream()
                .map(SensorDataMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SensorDataRecord> findLatestRecord() {
        return jpaRepository.findLatestRecord()
                .map(SensorDataMapper::toDomain);
    }

    @Override
    public boolean existsByDeviceIdAndCreatedAt(String deviceId, LocalDateTime createdAt) {
        return jpaRepository.existsByDeviceIdAndCreatedAt(deviceId, createdAt);
    }
}
