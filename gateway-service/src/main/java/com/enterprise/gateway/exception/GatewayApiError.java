package com.enterprise.gateway.exception;

import java.time.Instant;
import java.util.Map;

public record GatewayApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> validationErrors) {
}
