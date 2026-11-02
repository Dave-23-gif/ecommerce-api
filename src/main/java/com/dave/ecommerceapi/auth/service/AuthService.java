package com.dave.ecommerceapi.auth.service;

import com.dave.ecommerceapi.user.dto.request.LoginRequest;
import com.dave.ecommerceapi.user.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest request);
}