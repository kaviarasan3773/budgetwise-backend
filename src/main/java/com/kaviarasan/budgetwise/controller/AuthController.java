package com.kaviarasan.budgetwise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kaviarasan.budgetwise.dto.LoginRequest;
import com.kaviarasan.budgetwise.dto.RegisterRequest;
import com.kaviarasan.budgetwise.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public String registerUser(@RequestBody RegisterRequest request) {
        return authService.registerUser(request);
    }

    @PostMapping("/login")
    public String validateLogin(@RequestBody LoginRequest request) {
        String validateUser = authService.validateUser(request);
        return validateUser;
    }
}