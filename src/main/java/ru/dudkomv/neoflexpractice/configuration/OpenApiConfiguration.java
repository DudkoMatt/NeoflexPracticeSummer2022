package ru.dudkomv.neoflexpractice.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Value("${springdoc.security.scheme}")
    private String securityScheme;

    @Value("${springdoc.security.scheme-name}")
    private String securitySchemeName;

    @Bean
    public OpenAPI openAPI() {
        OpenAPI openAPI = new OpenAPI();
        openAPI.components(new Components()
                .addSecuritySchemes(securitySchemeName, createSecurityScheme()))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));

        return openAPI;
    }

    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme(securityScheme);
    }
}
