package com.example.cloud.security;

import com.example.cloud.exception.BadRequestException;
import com.example.cloud.model.User;
import com.example.cloud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        try {
            user = userService.getByLogin(username);
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
        return JwtEntityFactory.create(user);
    }
}