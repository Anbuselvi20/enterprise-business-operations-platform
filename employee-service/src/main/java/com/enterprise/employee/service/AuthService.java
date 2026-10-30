package com.enterprise.employee.service;

import com.enterprise.employee.dto.AuthResponse;
import com.enterprise.employee.dto.LoginRequest;
import com.enterprise.employee.dto.RegisterRequest;
import com.enterprise.employee.dto.UserResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    UserResponse getCurrentUser(String username);
}
