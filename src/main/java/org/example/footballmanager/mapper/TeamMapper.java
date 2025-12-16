package org.example.footballmanager.mapper;

import org.example.footballmanager.dto.request.TeamRequestDto;
import org.example.footballmanager.dto.response.TeamResponseDto;
import org.example.footballmanager.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TeamMapper {
    TeamResponseDto toDto(Team team);

    Team toEntity(TeamRequestDto dto);

    @Mapping(target = "id", ignore = true)
    void updateFromDto(TeamRequestDto dto, @MappingTarget Team team);
}
