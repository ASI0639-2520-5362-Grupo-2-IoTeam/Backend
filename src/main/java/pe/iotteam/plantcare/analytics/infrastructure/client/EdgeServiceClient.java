package pe.iotteam.plantcare.analytics.infrastructure.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pe.iotteam.plantcare.analytics.domain.exceptions.DataIngestionException;

import java.util.List;

/**
 * Client to consume external Edge Service API
 */
@Component
public class EdgeServiceClient {
    
    private static final Logger log = LoggerFactory.getLogger(EdgeServiceClient.class);

    private final RestTemplate restTemplate;
    
    @Value("${edge.service.base-url}")
    private String edgeServiceBaseUrl;

    public EdgeServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Fetch all sensor data from the external API
     * @return List of sensor data records
     * @throws DataIngestionException if the API call fails
     */
    public List<ExternalSensorDataDto> fetchAllSensorData() {
        try {
            log.info("Fetching sensor data from Edge Service: {}", edgeServiceBaseUrl);
            
            ResponseEntity<List<ExternalSensorDataDto>> response = restTemplate.exchange(
                    edgeServiceBaseUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ExternalSensorDataDto>>() {}
            );
            
            List<ExternalSensorDataDto> data = response.getBody();
            log.info("Successfully fetched {} records from Edge Service", 
                     data != null ? data.size() : 0);
            
            return data;
            
        } catch (RestClientException e) {
            log.error("Failed to fetch data from Edge Service: {}", e.getMessage(), e);
            throw new DataIngestionException("Failed to fetch data from external API", e);
        }
    }
}
