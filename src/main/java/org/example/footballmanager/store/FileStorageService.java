package org.example.footballmanager.store;

import jakarta.annotation.PostConstruct;
import org.example.footballmanager.type.FileType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

public abstract class FileStorageService {

    public abstract String getFolder();
    public abstract FileType getFileType();

    @PostConstruct
    private void init() {
        String folder = getFolder();
        if (folder == null || folder.isBlank()) return;

        Path uploadFolder = Paths.get(folder);

        try {
            Files.createDirectories(uploadFolder);

            if (Files.getFileStore(uploadFolder).supportsFileAttributeView("posix")) {
                Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxr-x---");
                Files.setPosixFilePermissions(uploadFolder, perms);
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot create folder: " + folder, e);
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
            Path uploadFolder = Paths.get(getFolder());
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

    public Resource loadAsResource(String filename) throws IOException {

        if (filename == null || filename.isBlank()) {
            throw new IllegalArgumentException("Filename is empty");
        }

        Path uploadFolder = Paths.get(getFolder());
        Path file = uploadFolder.resolve(filename).normalize();

        if (!file.startsWith(uploadFolder)) {
            throw new SecurityException("Invalid file path");
        }

        Resource resource = new UrlResource(file.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new FileNotFoundException("File not found");
        }

        return resource;
    }
}