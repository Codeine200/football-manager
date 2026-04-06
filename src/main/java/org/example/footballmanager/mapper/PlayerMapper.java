package org.example.footballmanager.mapper;

import org.example.footballmanager.domain.Player;
import org.example.footballmanager.dto.request.PlayerRequestDto;
import org.example.footballmanager.dto.response.PlayerResponseDto;
import org.example.footballmanager.entity.PlayerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
@Mapper(componentModel = "spring", uses = {TeamMapper.class, PlayerFileStorageMapper.class})
public abstract class PlayerMapper {

    @Mapping(target = "photo", source = "photo", qualifiedByName = "buildPlayerImageUrl")
    public abstract PlayerResponseDto toDto(PlayerEntity playerEntity);

    @Mapping(target = "teamId", expression = "java(new TeamId(dto.teamId()))")
    public abstract Player toDomain(PlayerRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "team", source = "teamId")
    public abstract PlayerEntity toEntity(Player player);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "team", ignore = true)
    public abstract void updateFromDto(PlayerRequestDto dto, @MappingTarget PlayerEntity entity);
}
