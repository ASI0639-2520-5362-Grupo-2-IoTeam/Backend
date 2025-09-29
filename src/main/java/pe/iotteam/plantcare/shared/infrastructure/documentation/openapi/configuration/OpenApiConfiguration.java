package pe.iotteam.plantcare.shared.infrastructure.documentation.openapi.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@Configuration
public class OpenApiConfiguration {
  @Bean
  public OpenAPI micasitaPlatformOpenApi() {
    // General configuration
    var openApi = new OpenAPI();
    openApi
            .info(new Info()
                    .title("MiCasita Platform API")
                    .description("MiCasita Platform application REST API documentation.")
                    .version("v1.0.0")
                    .license(new License().name("Apache 2.0")
                            .url("https://springdoc.org")))
            .externalDocs(new ExternalDocumentation()
                    .description("MiCasita Platform Documentation")
                    .url("https://github.com/SI729-2401-WS53-Grupo-MiCasita/Documentation"));
    return openApi;
  }
}
