package com.gatewayservice.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI gatewayOpenAPI() {
        // Define Bearer Token security scheme
        SecurityScheme bearerTokenScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        // Apply the security requirement
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("Bearer Token");

        return new OpenAPI()
                .info(new Info()
                        .title("Gateway API 명세서")
                        .description("Gateway에서 제공하는 API 명세서")
                        .version("v1"))
                .servers(List.of(
                        new Server().url("http://localhost:8111").description("Local Gateway Server"),
                        new Server().url("http://gachon-adore.duckdns.org:8111").description("Deployed Gateway Server")
                ))
                .components(new Components().addSecuritySchemes("Bearer Token", bearerTokenScheme))
                .addSecurityItem(securityRequirement);
    }
}