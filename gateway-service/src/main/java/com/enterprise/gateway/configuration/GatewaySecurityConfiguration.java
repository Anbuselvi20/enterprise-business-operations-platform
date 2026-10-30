package com.enterprise.gateway.configuration;

import com.enterprise.gateway.exception.GatewayApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfiguration {

    private static final String JWT_ISSUER = "enterprise-employee-service";

    @Bean
    public ReactiveJwtDecoder jwtDecoder(@Value("${security.jwt.secret}") String encodedSecret) {
        final byte[] secretBytes;
        try {
            secretBytes = Base64.getDecoder().decode(encodedSecret);
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException("security.jwt.secret must be Base64 encoded.", exception);
        }
        if (secretBytes.length < 32) {
            throw new IllegalStateException(
                    "security.jwt.secret must contain at least 32 decoded bytes.");
        }

        SecretKey secretKey = new SecretKeySpec(secretBytes, "HmacSHA256");
        NimbusReactiveJwtDecoder decoder = NimbusReactiveJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
        OAuth2TokenValidator<Jwt> validator =
                JwtValidators.createDefaultWithIssuer(JWT_ISSUER);
        decoder.setJwtValidator(validator);
        return decoder;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http,
            ServerAuthenticationEntryPoint authenticationEntryPoint,
            ServerAccessDeniedHandler accessDeniedHandler) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/auth/register", "/auth/login")
                        .permitAll()
                        .anyExchange()
                        .authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> {})
                        .authenticationEntryPoint(authenticationEntryPoint))
                .build();
    }

    @Bean
    public ServerAuthenticationEntryPoint gatewayAuthenticationEntryPoint(
            ObjectMapper objectMapper) {
        return (exchange, exception) -> writeError(
                exchange.getResponse(),
                objectMapper,
                HttpStatus.UNAUTHORIZED,
                "A valid bearer token is required.",
                exchange.getRequest().getPath().value());
    }

    @Bean
    public ServerAccessDeniedHandler gatewayAccessDeniedHandler(ObjectMapper objectMapper) {
        return (exchange, exception) -> writeError(
                exchange.getResponse(),
                objectMapper,
                HttpStatus.FORBIDDEN,
                "You do not have permission to access this resource.",
                exchange.getRequest().getPath().value());
    }

    private Mono<Void> writeError(
            org.springframework.http.server.reactive.ServerHttpResponse response,
            ObjectMapper objectMapper,
            HttpStatus status,
            String message,
            String path) {
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.getHeaders().setCacheControl("no-store");
        if (status == HttpStatus.UNAUTHORIZED) {
            response.getHeaders().set(HttpHeaders.WWW_AUTHENTICATE, "Bearer");
        }

        GatewayApiError error = new GatewayApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                Map.of());
        byte[] body;
        try {
            body = objectMapper.writeValueAsBytes(error);
        } catch (com.fasterxml.jackson.core.JsonProcessingException exception) {
            return Mono.error(exception);
        }
        DataBuffer buffer = response.bufferFactory().wrap(body);
        return response.writeWith(Mono.just(buffer));
    }
}
