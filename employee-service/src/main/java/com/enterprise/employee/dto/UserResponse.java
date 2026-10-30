package com.enterprise.employee.dto;

import com.enterprise.employee.entity.UserRole;

public record UserResponse(Long id, String username, String email, UserRole role) {
}
