package com.vn.tan.foodiesapi.controller;

import com.vn.tan.foodiesapi.controller.routes.UserRoute;
import com.vn.tan.foodiesapi.io.ApiResponse;
import com.vn.tan.foodiesapi.io.AuthenticationRequest;
import com.vn.tan.foodiesapi.io.UserRequest;
import com.vn.tan.foodiesapi.io.UserResponse;
import com.vn.tan.foodiesapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UserRoute.API_BASE_USER)
@CrossOrigin("*")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(UserRoute.REGISTER)
    public ResponseEntity<ApiResponse> Register(@RequestBody UserRequest userRequest) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            apiResponse.setStatusCode(HttpStatus.OK.value());
            apiResponse.setMessage("Register sucessfully");
            UserResponse result = this.userService.registerUser(userRequest);
            apiResponse.setData(result);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }
    }
}
