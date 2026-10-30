package com.enterprise.employee.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT")
@OpenAPIDefinition(
        info = @Info(
                title = "Employee Service API",
                version = "v1",
                description = "Create, retrieve, update, and delete employee records."))
public class OpenApiConfiguration {
}
