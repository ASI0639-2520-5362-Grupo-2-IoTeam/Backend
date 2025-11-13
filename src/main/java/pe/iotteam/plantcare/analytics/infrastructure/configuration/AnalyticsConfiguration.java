package pe.iotteam.plantcare.analytics.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration for Analytics bounded context
 */
@Configuration
@EnableScheduling
public class AnalyticsConfiguration {

    @Bean
    public RestTemplate analyticsRestTemplate() {
        return new RestTemplate();
    }
}
