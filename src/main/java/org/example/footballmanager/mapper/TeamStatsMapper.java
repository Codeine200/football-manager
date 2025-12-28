package org.example.footballmanager.mapper;

import org.example.footballmanager.dto.response.MatchResponseDto;
import org.example.footballmanager.entity.MatchStatsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TeamStatsMapper {

    TeamStatsMapper INSTANCE = Mappers.getMapper(TeamStatsMapper.class);

    @Mapping(target = "teamId", source = "team.id")
    @Mapping(target = "teamName", source = "team.name")
    MatchResponseDto.TeamStatsDto toDto(MatchStatsEntity stats);
}