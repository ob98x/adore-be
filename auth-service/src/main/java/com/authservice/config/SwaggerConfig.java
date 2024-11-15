package com.authservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@OpenAPIDefinition(
        info = @Info(title = "API 명세서",
                description = "CAPI 명세서",
                version = "v1"))
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI api() {
        // Define Bearer Token security scheme
        SecurityScheme bearerTokenScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP) // Change to HTTP type
                .scheme("bearer") // Specify "bearer" as the scheme
                .bearerFormat("JWT") // Optionally specify the token format
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        // Apply the security requirement
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("Bearer Token");

        return new OpenAPI()
                .servers(List.of(
                        new Server().url("http://localhost:8111").description("Local Server"),
                        new Server().url("http://gachon-adore.duckdns.org:8111").description("Deploy Server")
                ))
                .components(new Components().addSecuritySchemes("Bearer Token", bearerTokenScheme))
                .addSecurityItem(securityRequirement);
    }
}
