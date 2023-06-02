package com.example.cloud.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JwtResponse {

    @JsonProperty("auth-token")
    private String authToken;
}