package org.example.footballmanager.service;

import org.example.footballmanager.config.UploadProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private final Path uploadFolder;

    public FileStorageService(UploadProperties properties) {
        this.uploadFolder = Paths.get(properties.getFolder());
        try {
            Files.createDirectories(uploadFolder);
        } catch (IOException e) {
            throw new RuntimeException("Cannot create upload folder", e);
        }
    }

    public String saveFile(MultipartFile file, String newName) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        try {
            String originalName = file.getOriginalFilename();
            String ext = "";

            if (originalName != null && originalName.contains(".")) {
                ext = originalName.substring(originalName.lastIndexOf("."));
            }

            String filename = newName + ext;
            Path filePath = uploadFolder.resolve(filename).normalize();

            if (!filePath.startsWith(uploadFolder)) {
                throw new SecurityException("Invalid file path");
            }

            file.transferTo(filePath);

            return filename;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}