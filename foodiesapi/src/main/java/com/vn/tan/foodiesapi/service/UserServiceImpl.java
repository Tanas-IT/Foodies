package com.vn.tan.foodiesapi.service;

import com.vn.tan.foodiesapi.entities.User;
import com.vn.tan.foodiesapi.io.UserRequest;
import com.vn.tan.foodiesapi.io.UserResponse;
import com.vn.tan.foodiesapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationFacade authenticationFacade;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationFacade authenticationFacade) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    public UserResponse registerUser(UserRequest request) {
       User newUser = convertToUser(request);
       newUser = this.userRepository.save(newUser);
       return convertToResponse(newUser);
    }

    @Override
    public String findByUserId() {
        String loggedInUserEmail = authenticationFacade.getAuthentication().getName();
        User loggedInUser =  userRepository.findByEmail(loggedInUserEmail).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return loggedInUser.getId();
    }

    private User convertToUser(UserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setRole(request.getRole());
        return user;
    }

    private UserResponse convertToResponse(User registedUser) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(registedUser.getId());
        userResponse.setName(registedUser.getName());
        userResponse.setEmail(registedUser.getEmail());
        userResponse.setRole(registedUser.getRole());
        return userResponse;
    }
}
