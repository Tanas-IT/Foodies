package com.vn.tan.foodiesapi.service;

import com.vn.tan.foodiesapi.io.UserRequest;
import com.vn.tan.foodiesapi.io.UserResponse;

public interface UserService {

    UserResponse registerUser(UserRequest request);
    String findByUserId();
}
