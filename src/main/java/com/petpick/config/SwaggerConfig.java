package com.petpick.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server; // Import Server
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List; // Import List

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);

        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );

        // Create Server object and set HTTPS URL
        Server server = new Server();
        server.setUrl("https://back.petpick.store"); // Replace with your actual domain

        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");

        // Build and return OpenAPI object
        return new OpenAPI()
                .components(components)
                .info(apiInfo())
                .addSecurityItem(securityRequirement)
                .servers(List.of(server, localServer)); // Add the server here
    }

    private Info apiInfo() {
        return new Info()
                .title("API Test") // API title
                .description("Swagger UI is on") // API description
                .version("1.0.0"); // API version
    }
}
