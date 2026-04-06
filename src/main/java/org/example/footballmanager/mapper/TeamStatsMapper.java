package org.example.footballmanager.mapper;

import org.example.footballmanager.dto.response.MatchResponseDto;
import org.example.footballmanager.entity.MatchStatsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" , uses = {TeamFileStorageMapper.class})
public interface TeamStatsMapper {

    @Mapping(target = "id", source = "team.id")
    @Mapping(target = "name", source = "team.name")
    @Mapping(target = "imageUrl", source = "team.logo", qualifiedByName = "buildImageUrl")
    MatchResponseDto.TeamStatsDto toDto(MatchStatsEntity stats);

}