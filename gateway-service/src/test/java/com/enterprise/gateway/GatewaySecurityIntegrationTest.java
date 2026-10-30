package com.enterprise.gateway;

import static org.springframework.test.web.reactive.server.WebTestClient.bindToApplicationContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(properties = "security.jwt.secret=MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=")
class GatewaySecurityIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = bindToApplicationContext(applicationContext).build();
    }

    @Test
    void shouldRequireJwtForProtectedRoutes() {
        webTestClient.get()
                .uri("/employees")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("A valid bearer token is required.");
    }

    @Test
    void shouldRejectInvalidJwtForProtectedRoutes() {
        webTestClient.get()
                .uri("/projects")
                .headers(headers -> headers.setBearerAuth("not-a-valid-token"))
                .exchange()
                .expectStatus().isUnauthorized();
    }
}
