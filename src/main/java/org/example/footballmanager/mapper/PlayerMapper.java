package org.example.footballmanager.mapper;

import org.example.footballmanager.domain.Player;
import org.example.footballmanager.domain.TeamId;
import org.example.footballmanager.dto.request.PlayerRequestDto;
import org.example.footballmanager.dto.response.PlayerResponseDto;
import org.example.footballmanager.entity.PlayerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = TeamMapper.class)
public interface PlayerMapper {

    @Mapping(target = "photo", source = "photo", qualifiedByName = "addPhotoPath")
    PlayerResponseDto toDto(PlayerEntity playerEntity);

    @Mapping(target = "teamId", expression = "java(new TeamId(dto.teamId()))")
    Player toDomain(PlayerRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "team", source = "teamId")
    PlayerEntity toEntity(Player player);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "team", ignore = true)
    void updateFromDto(PlayerRequestDto dto, @MappingTarget PlayerEntity entity);

    default Long mapTeamId(TeamId teamId) {
        return teamId != null ? teamId.id() : null;
    }

    @Named("addPhotoPath")
    default String addPhotoPath(String photoFileName) {
        if (photoFileName == null || photoFileName.isBlank()) return null;
        return "/files/players/" + photoFileName;
    }
}
