package com.enterprise.client.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Client Service API",
                version = "v1",
                description = "Manage enterprise client records."))
public class OpenApiConfiguration {
}
