package com.example.cloud.controller;

import com.example.cloud.exception.BadRequestException;
import com.example.cloud.exception.InternalServerErrorException;
import com.example.cloud.exception.UnauthorizedException;
import com.example.cloud.model.UserFileRequest;
import com.example.cloud.model.UserFileResponse;
import com.example.cloud.service.UserFileService;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
public class UserFileController {

    private final UserFileService userFileService;

    public UserFileController(UserFileService userFileService) {
        this.userFileService = userFileService;
    }

    @GetMapping("/list")
    public List<UserFileResponse> listAll(@RequestParam int limit, @RequestHeader("auth-token") String token) throws UnauthorizedException {
        return userFileService.listAll(limit, token);
    }

    @GetMapping("/file")
    public Resource download(@RequestParam String filename, @RequestHeader("auth-token") String token) throws BadRequestException, UnauthorizedException {
        return userFileService.download(filename, token);
    }

    @PostMapping("/file")
    public void upload(@RequestParam("file") MultipartFile file, @RequestParam String filename, @RequestHeader("auth-token") String token) throws BadRequestException, UnauthorizedException {
        userFileService.upload(file, filename, token);
    }

    @PutMapping("/file")
    public void update(@RequestBody UserFileRequest request, @RequestParam String filename, @RequestHeader("auth-token") String token) throws InternalServerErrorException, BadRequestException, UnauthorizedException {
        userFileService.update(request, filename, token);
    }

    @DeleteMapping("/file")
    public void remove(@RequestParam String filename, @RequestHeader("auth-token") String token) throws InternalServerErrorException, BadRequestException, UnauthorizedException {
        userFileService.remove(filename, token);
    }
}