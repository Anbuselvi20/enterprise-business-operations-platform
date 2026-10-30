package com.enterprise.gateway.filter;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        long startTime = System.nanoTime();

        return chain.filter(exchange)
                .doFinally(signalType -> {
                    long durationMillis = TimeUnit.NANOSECONDS.toMillis(
                            System.nanoTime() - startTime);
                    String status = exchange.getResponse().getStatusCode() == null
                            ? "not_committed"
                            : Integer.toString(exchange.getResponse().getStatusCode().value());
                    log.info("gateway_request method={} path={} status={} duration_ms={} signal={}",
                            request.getMethod(),
                            request.getPath().value(),
                            status,
                            durationMillis,
                            signalType);
                });
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
