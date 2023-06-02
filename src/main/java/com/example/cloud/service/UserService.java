package com.example.cloud.service;

import com.example.cloud.exception.BadRequestException;
import com.example.cloud.model.User;
import com.example.cloud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getByLogin(String name) throws BadRequestException {
        User user = userRepository.findByLogin(name);
        if (user != null) {
            return user;
        } else {
            throw new BadRequestException("BAD REQUEST");
        }
    }
}