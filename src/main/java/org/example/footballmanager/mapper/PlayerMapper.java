package org.example.footballmanager.mapper;

import org.example.footballmanager.dto.request.PlayerRequestDto;
import org.example.footballmanager.dto.response.PlayerResponseDto;
import org.example.footballmanager.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = TeamMapper.class)
public interface PlayerMapper {

    PlayerResponseDto toDto(Player player);

    @Mapping(target = "team", ignore = true)
    //    @Mapping(target = "id", ignore = true)
    Player toEntity(PlayerRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "team", ignore = true)
    void updateFromDto(PlayerRequestDto dto, @MappingTarget Player entity);
}
