package com.example.cloud.model;

import lombok.Data;

@Data
public class UserFileResponse {

    private String filename;
    private Long size;
}