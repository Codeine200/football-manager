package org.example.footballmanager.mapper;

import lombok.RequiredArgsConstructor;
import org.example.footballmanager.store.TeamFileStorageService;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamFileStorageMapper {

    private final TeamFileStorageService teamFileStorageService;

    @Named("buildImageUrl")
    public String buildImageUrl(String logo) {
        if (logo == null || logo.isBlank()) return null;

        return "/files/"
               + teamFileStorageService.getFileType().getFolderName() + "/"
               + logo;
    }
}
