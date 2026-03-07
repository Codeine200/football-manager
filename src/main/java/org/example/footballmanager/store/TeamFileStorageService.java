package org.example.footballmanager.store;

import lombok.RequiredArgsConstructor;
import org.example.footballmanager.config.UploadProperties;
import org.example.footballmanager.type.FileType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamFileStorageService extends ImageStorageService {

    private final UploadProperties properties;

    @Override
    public String getFolder() {
        return properties.getTeamsPhotosFolder();
    }

    @Override
    public FileType getFileType() {
        return FileType.TEAMS;
    }
}
