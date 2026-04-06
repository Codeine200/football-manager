package org.example.footballmanager.mapper;

import org.example.footballmanager.domain.TeamData;
import org.example.footballmanager.dto.request.TeamRequestDto;
import org.example.footballmanager.dto.response.TeamResponseDto;
import org.example.footballmanager.entity.PlayerEntity;
import org.example.footballmanager.entity.TeamEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {TeamFileStorageMapper.class, PlayerFileStorageMapper.class})
public abstract class TeamMapper {

    @Mapping(target = "imageUrl", source = "logo", qualifiedByName = "buildImageUrl")
    public abstract TeamResponseDto toDto(TeamEntity teamEntity);

    @Mapping(target = "photo", source = "photo", qualifiedByName = "buildPlayerImageUrl")
    public abstract TeamResponseDto.Player toPlayer(PlayerEntity player);

    public abstract TeamEntity toEntity(TeamRequestDto dto);

    @Mapping(target = "imageUrl", source = "logo", qualifiedByName = "buildImageUrl")
    public abstract TeamData toDomain(TeamEntity dto);

    @Mapping(target = "id", ignore = true)
    public abstract void updateFromDto(TeamRequestDto dto, @MappingTarget TeamEntity teamEntity);
}
