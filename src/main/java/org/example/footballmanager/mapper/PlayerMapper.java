package org.example.footballmanager.mapper;

import org.example.footballmanager.dto.request.PlayerRequestDto;
import org.example.footballmanager.dto.response.PlayerResponseDto;
import org.example.footballmanager.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    @Mapping(source = "team.id", target = "teamId")
    PlayerResponseDto toDto(Player player);

    @Mapping(target = "team", ignore = true)
    Player toEntity(PlayerRequestDto dto);
}
