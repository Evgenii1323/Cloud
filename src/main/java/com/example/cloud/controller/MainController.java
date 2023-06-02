package com.example.cloud.controller;

import com.example.cloud.exception.BadRequestException;
import com.example.cloud.exception.InternalServerErrorException;
import com.example.cloud.model.UserFileRequest;
import com.example.cloud.model.UserFileResponse;
import com.example.cloud.service.UserFileService;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
public class MainController {

    private final UserFileService userFileService;

    public MainController(UserFileService userFileService) {
        this.userFileService = userFileService;
    }

    @GetMapping("/list")
    public List<UserFileResponse> list(@RequestParam int limit, Principal principal) {
        return userFileService.list(limit, principal);
    }

    @GetMapping("/file")
    public Resource download(@RequestParam String filename, Principal principal) throws BadRequestException {
        return userFileService.download(filename, principal);
    }

    @PostMapping("/file")
    public void upload(@RequestParam("file") MultipartFile file, @RequestParam String filename, Principal principal) throws BadRequestException {
        userFileService.upload(file, filename, principal);
    }

    @PutMapping("/file")
    public void update(@RequestBody UserFileRequest request, @RequestParam String filename, Principal principal) throws InternalServerErrorException, BadRequestException {
        userFileService.update(request, filename, principal);
    }

    @DeleteMapping("/file")
    public void remove(@RequestParam String filename, Principal principal) throws InternalServerErrorException, BadRequestException {
        userFileService.remove(filename, principal);
    }
}