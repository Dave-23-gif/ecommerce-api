package com.dave.ecommerceapi.user.service;

import com.dave.ecommerceapi.user.dto.request.RegistrationRequest;
import com.dave.ecommerceapi.user.dto.response.UserResponse;

public interface UserService {
    UserResponse register(RegistrationRequest request);
}
