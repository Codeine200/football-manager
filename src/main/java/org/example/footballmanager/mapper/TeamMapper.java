package org.example.footballmanager.mapper;

import org.example.footballmanager.dto.request.TeamRequestDto;
import org.example.footballmanager.dto.response.TeamResponseDto;
import org.example.footballmanager.entity.Team;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamMapper {
    TeamResponseDto toDto(Team team);
    Team toEntity(TeamRequestDto dto);
}
