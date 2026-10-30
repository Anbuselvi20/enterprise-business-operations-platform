package com.enterprise.gateway.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Order(-2)
public class GlobalExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable exception) {
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(exception);
        }

        HttpStatus status;
        String message;
        if (exception instanceof WebClientRequestException) {
            status = HttpStatus.BAD_GATEWAY;
            message = "The requested service is currently unavailable.";
            log.warn("Upstream unavailable for request {} {}",
                    exchange.getRequest().getMethod(),
                    exchange.getRequest().getPath().value());
        } else if (exception instanceof ServerWebInputException) {
            status = HttpStatus.BAD_REQUEST;
            message = "The request is invalid.";
        } else if (exception instanceof NotFoundException) {
            status = HttpStatus.NOT_FOUND;
            message = "Route or resource not found.";
        } else if (exception instanceof ResponseStatusException responseStatusException) {
            status = HttpStatus.valueOf(responseStatusException.getStatusCode().value());
            message = status.is5xxServerError()
                    ? "The request could not be completed."
                    : responseStatusException.getReason();
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "An unexpected gateway error occurred.";
            log.error("Unexpected gateway error for request {} {}",
                    exchange.getRequest().getMethod(),
                    exchange.getRequest().getPath().value(),
                    exception);
        }

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        exchange.getResponse().getHeaders().setCacheControl("no-store");
        if (status == HttpStatus.UNAUTHORIZED) {
            exchange.getResponse().getHeaders().set(HttpHeaders.WWW_AUTHENTICATE, "Bearer");
        }

        GatewayApiError error = new GatewayApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                exchange.getRequest().getPath().value(),
                Map.of());
        final byte[] body;
        try {
            body = objectMapper.writeValueAsBytes(error);
        } catch (IOException serializationException) {
            return Mono.error(serializationException);
        }

        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(body);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
