package pe.iotteam.plantcare.shared.infrastructure.documentation.openapi.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI micasitaPlatformOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Plantcare API")
                        .description("Plantcare Platform application REST API documentation.")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.8.13")
                                .url("https://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Plantcare Platform Documentation")
                        .url("https://github.com/ASI0639-2520-5362-Grupo-2-IoTeam/Documentation"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("bearerAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}