package com.vn.tan.foodiesapi.controller;

import com.vn.tan.foodiesapi.controller.routes.UserRoute;
import com.vn.tan.foodiesapi.io.ApiResponse;
import com.vn.tan.foodiesapi.io.AuthenticationRequest;
import com.vn.tan.foodiesapi.io.AuthenticationResponse;
import com.vn.tan.foodiesapi.service.AppUserDetailsService;
import com.vn.tan.foodiesapi.util.JwtUitl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UserRoute.API_BASE_USER)
@CrossOrigin("*")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private final AppUserDetailsService appUserDetailsService;
    private final JwtUitl jwtUitls;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, AppUserDetailsService appUserDetailsService, JwtUitl jwtUitls) {
        this.authenticationManager = authenticationManager;
        this.appUserDetailsService = appUserDetailsService;
        this.jwtUitls = jwtUitls;
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
            final UserDetails userDetails =  appUserDetailsService.loadUserByUsername(authenticationRequest.getEmail());
            final String accessToken = jwtUitls.generateAccessToken(userDetails);
            final String refreshToken = jwtUitls.generateRefreshToken(userDetails);

            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setEmail(authenticationRequest.getEmail());
            authenticationResponse.setAccessToken(accessToken);
            authenticationResponse.setRefreshToken(refreshToken);

            apiResponse.setStatusCode(HttpStatus.OK.value());
            apiResponse.setMessage("Login successfully");
            apiResponse.setData(authenticationResponse);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

        } catch (Exception ex) {
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }
    }
}
