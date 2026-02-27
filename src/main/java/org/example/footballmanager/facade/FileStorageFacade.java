package org.example.footballmanager.facade;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.footballmanager.store.FileStorageService;
import org.example.footballmanager.type.FileType;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FileStorageFacade {

    private final List<FileStorageService> fileStorageServiceList;

    private Map<FileType, FileStorageService> storageServiceMap;

    @PostConstruct
    public void init() {
        storageServiceMap = new HashMap<>();
        for (FileStorageService service : fileStorageServiceList) {
            String folder = service.getFolder();
            FileType fileType = service.getFileType();
            if (folder != null && !folder.isBlank() && fileType != null) {
                storageServiceMap.put(fileType, service);
            } else {
                throw new IllegalStateException("FileStorageService " + service.getClass().getSimpleName() +
                        " returned null or empty folder path or type");
            }
        }
    }

    public Resource loadFile(String folder, String filename) throws IOException {

        if (folder == null || folder.isBlank()) {
            throw new IllegalArgumentException("Folder is empty");
        }

        FileStorageService service = storageServiceMap.get(FileType.fromString(folder));

        if (service == null) {
            throw new IllegalArgumentException("No FileStorageService found for folder: " + folder);
        }

        return service.loadAsResource(filename);
    }
}