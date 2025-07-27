package com.example.task.manager.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    public String uploadDir;

    public String storeFile(MultipartFile file) throws IOException{
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

        Path uploadPath = Paths.get(uploadDir);

        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(originalFileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toAbsolutePath().toString();
    }

    public UrlResource loadFileAsResource(String filePath) throws IOException{
        Path path = Paths.get(filePath).normalize();

        UrlResource resource = new UrlResource(path.toUri());

        if(resource.exists() && resource.isReadable()){
            return resource;
        }
        else{
            throw new RuntimeException("File Not Found at Path: "+ filePath);
        }
    }
}
