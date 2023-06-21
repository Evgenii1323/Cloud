package com.example.cloud.service;

import com.example.cloud.exception.BadRequestException;
import com.example.cloud.exception.InternalServerErrorException;
import com.example.cloud.exception.UnauthorizedException;
import com.example.cloud.model.UserFile;
import com.example.cloud.model.UserFileRequest;
import com.example.cloud.model.UserFileResponse;
import com.example.cloud.repository.AuthRepository;
import com.example.cloud.repository.UserFileRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

@Service
public class UserFileService {

    private final UserFileRepository userFileRepository;
    private final AuthRepository authRepository;
    private static final String LOCATION = "./storage";

    public UserFileService(UserFileRepository userFileRepository, AuthRepository authRepository) {
        this.userFileRepository = userFileRepository;
        this.authRepository = authRepository;
    }

    @Transactional
    public List<UserFileResponse> listAll(int limit, String token) throws UnauthorizedException {
        String login = authRepository.getLogin(token);
        if (login == null) {
            throw new UnauthorizedException("UNAUTHORIZED");
        }
        List<UserFile> userFiles = userFileRepository.findAllWithLimit(limit, login);
        List<UserFileResponse> list = new LinkedList<>();
        for (UserFile userFile : userFiles) {
            UserFileResponse file = new UserFileResponse();
            file.setFilename(userFile.getFilename());
            file.setSize(userFile.getSize());
            list.add(file);
        }
        return list;
    }

    @Transactional
    public Resource download(String filename, String token) throws BadRequestException, UnauthorizedException {
        String login = authRepository.getLogin(token);
        if (login == null) {
            throw new UnauthorizedException("UNAUTHORIZED");
        }
        UserFile userFile = userFileRepository.findByFilenameAndUser(filename, login);
        if (userFile != null) {
            try {
                File catalog = createCatalog(token);
                File file = new File(catalog + File.separator + filename);
                Path path = Paths.get(file.getAbsolutePath());
                Resource resource = new UrlResource(path.toUri());
                if (resource.exists() || resource.isReadable()) {
                    return resource;
                } else {
                    throw new InternalServerErrorException("INTERNAL SERVER ERROR");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new BadRequestException("BAD REQUEST");
        }
        return null;
    }

    @Transactional
    public void upload(MultipartFile multipartFile, String filename, String token) throws BadRequestException, UnauthorizedException {
        String login = authRepository.getLogin(token);
        if (login == null) {
            throw new UnauthorizedException("UNAUTHORIZED");
        }
        UserFile userFile = new UserFile();
        UserFile userFile1 = userFileRepository.findByFilenameAndUser(filename, login);
        if (!multipartFile.isEmpty() && userFile1 == null) {
            try {
                File catalog = createCatalog(token);
                File file = new File(catalog.getAbsolutePath() + File.separator + filename);
                multipartFile.transferTo(file);
                userFile.setFilename(filename);
                userFile.setSize(multipartFile.getSize());
                userFile.setUser(login);
                userFileRepository.save(userFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new BadRequestException("BAD REQUEST");
        }
    }

    @Transactional
    public void update(UserFileRequest request, String filename, String token) throws InternalServerErrorException, BadRequestException, UnauthorizedException {
        String login = authRepository.getLogin(token);
        if (login == null) {
            throw new UnauthorizedException("UNAUTHORIZED");
        }
        UserFile userFile = userFileRepository.findByFilenameAndUser(filename, login);
        UserFile userFile1 = userFileRepository.findByFilenameAndUser(request.getFilename(), login);
        if (userFile != null && userFile1 == null) {
            userFile.setFilename(request.getFilename());
            userFileRepository.save(userFile);
            File catalog = createCatalog(token);
            File file = new File(catalog + File.separator + filename);
            File newFile = new File(catalog + File.separator + request.getFilename());
            if (!file.renameTo(newFile)) {
                throw new InternalServerErrorException("INTERNAL SERVER ERROR");
            }
        } else {
            throw new BadRequestException("BAD REQUEST");
        }
    }

    @Transactional
    public void remove(String filename, String token) throws InternalServerErrorException, BadRequestException, UnauthorizedException {
        String login = authRepository.getLogin(token);
        if (login == null) {
            throw new UnauthorizedException("UNAUTHORIZED");
        }
        UserFile userFile = userFileRepository.findByFilenameAndUser(filename, login);
        if (userFile != null) {
            userFileRepository.deleteByFilenameAndUser(filename, login);
            File catalog = createCatalog(token);
            File file = new File(catalog + File.separator + filename);
            Path path = Paths.get(file.getAbsolutePath());
            if (!FileSystemUtils.deleteRecursively(path.toFile())) {
               throw new InternalServerErrorException("INTERNAL SERVER ERROR");
            }
        } else {
            throw new BadRequestException("BAD REQUEST");
        }
    }

    public File createCatalog(String token) {
        String login = authRepository.getLogin(token);
        File catalog = new File(LOCATION + File.separator + login);
        if(!catalog.exists()) {
            catalog.mkdirs();
        }
        return catalog;
    }
}