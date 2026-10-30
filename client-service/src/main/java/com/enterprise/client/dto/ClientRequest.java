package com.enterprise.client.dto;

import com.enterprise.client.entity.ClientStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClientRequest(
        @NotBlank @Size(max = 150) String clientName,
        @NotBlank @Size(max = 100) String industry,
        @NotBlank @Size(max = 150) String contactPerson,
        @NotBlank @Email @Size(max = 254) String email,
        @Size(max = 30) String phoneNumber,
        @NotBlank @Size(max = 100) String country,
        @NotNull ClientStatus status) {
}
