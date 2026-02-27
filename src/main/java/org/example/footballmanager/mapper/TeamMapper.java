package org.example.footballmanager.mapper;

import org.example.footballmanager.dto.request.TeamRequestDto;
import org.example.footballmanager.dto.response.TeamResponseDto;
import org.example.footballmanager.entity.TeamEntity;
import org.example.footballmanager.store.TeamFileStorageService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class TeamMapper {

    @Autowired
    protected TeamFileStorageService teamFileStorageService;

    @Mapping(target = "imageUrl", source = "logo", qualifiedByName = "buildImageUrl")
    public abstract TeamResponseDto toDto(TeamEntity teamEntity);

    public abstract TeamEntity toEntity(TeamRequestDto dto);

    @Mapping(target = "id", ignore = true)
    public abstract void updateFromDto(TeamRequestDto dto, @MappingTarget TeamEntity teamEntity);

    @Named("buildImageUrl")
    protected String buildImageUrl(String logo) {
        if (logo == null || logo.isBlank()) {
            return null;
        }

        return  "/files/"
                + teamFileStorageService.getFileType().getFolderName() + "/"
                + logo;
    }
}
