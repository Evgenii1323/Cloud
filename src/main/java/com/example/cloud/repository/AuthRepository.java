package com.example.cloud.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AuthRepository {

    private final Map<String, String> tokens = new ConcurrentHashMap<>();

    public void save(String login, String token) {
        tokens.put(token, login);
    }

    public void delete(String token) {
        String subToken = token.substring(7);
        tokens.remove(subToken);
    }

    public String getLogin(String token) {
        String subToken = token.substring(7);
        String login = tokens.get(subToken);
        return login;
    }
}