package pe.iotteam.plantcare.plant.infrastructure.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pe.iotteam.plantcare.plant.interfaces.rest.resources.SensorData;

import java.util.Optional;

@Component
public class EdgeServiceClientImpl implements EdgeServiceClient {

    private final RestTemplate restTemplate;
    private final String edgeServiceUrl;

    public EdgeServiceClientImpl(RestTemplate restTemplate, @Value("${edge.service.base-url}") String edgeServiceUrl) {
        this.restTemplate = restTemplate;
        this.edgeServiceUrl = edgeServiceUrl;
    }

    @Override
    public Optional<SensorData> getLatestSensorData() {
        try {
            SensorData response = restTemplate.getForObject(edgeServiceUrl, SensorData.class);
            return Optional.ofNullable(response);
        } catch (Exception e) {
            // Log the exception or handle it as per application's error handling strategy
            return Optional.empty();
        }
    }
}
