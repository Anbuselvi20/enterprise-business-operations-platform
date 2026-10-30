package com.enterprise.project.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Project Service API",
                version = "v1",
                description = "Create and manage enterprise projects."))
public class OpenApiConfiguration {
}
