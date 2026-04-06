package org.example.footballmanager.mapper;

import lombok.RequiredArgsConstructor;
import org.example.footballmanager.store.PlayerFileStorageService;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerFileStorageMapper {

    private final PlayerFileStorageService playerFileStorageService;

    @Named("buildPlayerImageUrl")
    public String buildPlayerImageUrl(String logo) {
        if (logo == null || logo.isBlank()) return null;

        return "/files/"
                + playerFileStorageService.getFileType().getFolderName() + "/"
                + logo;
    }
}
