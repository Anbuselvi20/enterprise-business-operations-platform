package com.enterprise.employee.dto;

import com.enterprise.employee.dto.validation.BcryptPasswordLength;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank
                @Size(min = 3, max = 50)
                @Pattern(regexp = "^[A-Za-z0-9._-]+$", message = "contains unsupported characters")
                String username,
        @NotBlank @Email @Size(max = 254) String email,
        @NotBlank @Size(min = 12, max = 72) @BcryptPasswordLength String password) {
}
