package com.example.cloud.service;

import com.example.cloud.exception.BadRequestException;
import com.example.cloud.exception.InternalServerErrorException;
import com.example.cloud.model.UserFile;
import com.example.cloud.model.UserFileRequest;
import com.example.cloud.model.UserFileResponse;
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
import java.security.Principal;
import java.util.LinkedList;
import java.util.List;

@Service
public class UserFileService {

    private final UserFileRepository userFileRepository;
    private static final String LOCATION = "./src/main/resources/static";

    public UserFileService(UserFileRepository userFileRepository) {
        this.userFileRepository = userFileRepository;
    }

    @Transactional
    public List<UserFileResponse> list(int limit, Principal principal) {
        String user = principal.getName();
        List<UserFile> userFiles = userFileRepository.findAllWithLimit(limit, user);
        List<UserFileResponse> list = new LinkedList<>();
        for (UserFile file : userFiles) {
            UserFileResponse file1 = new UserFileResponse();
            file1.setFilename(file.getFilename());
            file1.setSize(file.getSize());
            list.add(file1);
        }
        return list;
    }

    @Transactional
    public Resource download(String filename, Principal principal) throws BadRequestException {
        UserFile userFile = userFileRepository.findByFilenameAndUser(filename, principal.getName());
        if (userFile != null) {
            try {
                File catalog = createCatalog(principal);
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
    public void upload(MultipartFile multipartFile, String filename, Principal principal) throws BadRequestException {
        UserFile userFile = new UserFile();
        UserFile userFile1 = userFileRepository.findByFilenameAndUser(filename, principal.getName());
        if (!multipartFile.isEmpty() && userFile1 == null) {
            try {
                File catalog = createCatalog(principal);
                File file = new File(catalog.getAbsolutePath() + File.separator + filename);
                multipartFile.transferTo(file);
                userFile.setFilename(filename);
                userFile.setSize(multipartFile.getSize());
                userFile.setUser(principal.getName());
                userFileRepository.save(userFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new BadRequestException("BAD REQUEST");
        }
    }

    @Transactional
    public void update(UserFileRequest request, String filename, Principal principal) throws InternalServerErrorException, BadRequestException {
        UserFile userFile = userFileRepository.findByFilenameAndUser(filename, principal.getName());
        UserFile userFile1 = userFileRepository.findByFilenameAndUser(request.getFilename(), principal.getName());
        if (userFile != null && userFile1 == null) {
            userFile.setFilename(request.getFilename());
            userFileRepository.save(userFile);
            File catalog = createCatalog(principal);
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
    public void remove(String filename, Principal principal) throws InternalServerErrorException, BadRequestException {
        UserFile userFile = userFileRepository.findByFilenameAndUser(filename, principal.getName());
        if (userFile != null) {
            userFileRepository.deleteByFilenameAndUser(filename, principal.getName());
            File catalog = createCatalog(principal);
            File file = new File(catalog + File.separator + filename);
            Path path = Paths.get(file.getAbsolutePath());
            if (!FileSystemUtils.deleteRecursively(path.toFile())) {
               throw new InternalServerErrorException("INTERNAL SERVER ERROR");
            }
        } else {
            throw new BadRequestException("BAD REQUEST");
        }
    }

    private static File createCatalog(Principal principal) {
        String user = principal.getName();
        File catalog = new File(LOCATION + File.separator + user);
        if(!catalog.exists()) {
            catalog.mkdirs();
        }
        return catalog;
    }
}