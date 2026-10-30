package com.enterprise.client.dto;

import com.enterprise.client.entity.ClientStatus;

public record ClientResponse(
        Long id,
        String clientName,
        String industry,
        String contactPerson,
        String email,
        String phoneNumber,
        String country,
        ClientStatus status) {
}
