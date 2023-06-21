package com.example.cloud.service;

import com.example.cloud.exception.BadRequestException;
import com.example.cloud.model.Role;
import com.example.cloud.model.User;
import com.example.cloud.repository.AuthRepository;
import com.example.cloud.security.JwtRequest;
import com.example.cloud.security.JwtResponse;
import com.example.cloud.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final AuthRepository authRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtResponse login(JwtRequest jwtRequest) throws BadRequestException {
        JwtResponse jwtResponse = new JwtResponse();
        Set<Role> set = new HashSet<>();
        set.add(Role.USER);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getLogin(), jwtRequest.getPassword()));
        User user = userService.getByLogin(jwtRequest.getLogin());
        jwtResponse.setAuthToken(jwtTokenProvider.createAccessToken(user.getId(), user.getLogin(), set));
        authRepository.save(jwtRequest.getLogin(), jwtResponse.getAuthToken());
        return jwtResponse;
    }

    public void logout(String token) {
        authRepository.delete(token);
    }
}