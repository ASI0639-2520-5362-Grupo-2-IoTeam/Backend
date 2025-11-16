package pe.iotteam.plantcare.plant.infrastructure.clients;

import pe.iotteam.plantcare.plant.interfaces.rest.resources.SensorData;
import java.util.Optional;

/**
 * Client to interact with the Edge Service.
 */
public interface EdgeServiceClient {
    /**
     * Fetches the latest sensor data from the edge service.
     * @return an Optional containing the SensorData if available, otherwise empty.
     */
    Optional<SensorData> getLatestSensorData();
}
