package com.example.cloud.controller;

import com.example.cloud.exception.BadRequestException;
import com.example.cloud.security.JwtRequest;
import com.example.cloud.security.JwtResponse;
import com.example.cloud.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public JwtResponse login(@RequestBody JwtRequest jwtRequest) throws BadRequestException {
        return authService.login(jwtRequest);
    }
}