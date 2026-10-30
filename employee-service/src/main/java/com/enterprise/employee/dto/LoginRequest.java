package com.enterprise.employee.dto;

import com.enterprise.employee.dto.validation.BcryptPasswordLength;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank @Size(max = 50) String username,
        @NotBlank @Size(max = 72) @BcryptPasswordLength String password) {
}
