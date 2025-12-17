package org.example.footballmanager.mapper;

import lombok.RequiredArgsConstructor;
import org.example.footballmanager.dto.request.MatchCreateRequestDto;
import org.example.footballmanager.dto.response.MatchResponseDto;
import org.example.footballmanager.entity.Match;
import org.example.footballmanager.entity.MatchStats;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


@Mapper(componentModel = "spring", uses = {TeamStatsMapper.class})
public interface MatchMapper {

    Match toEntity(MatchCreateRequestDto dto);

    @Mapping(target = "team1", source = "match", qualifiedByName = "mapTeam1")
    @Mapping(target = "team2", source = "match", qualifiedByName = "mapTeam2")
    MatchResponseDto toDto(Match match);

    @Named("mapTeam1")
    default MatchResponseDto.TeamStatsDto mapTeam1(Match match) {
        return match.getStats().stream()
                .filter(MatchStats::isGuest)
                .findFirst()
                .map(TeamStatsMapper.INSTANCE::toDto)
                .orElse(null);
    }

    @Named("mapTeam2")
    default  MatchResponseDto.TeamStatsDto mapTeam2(Match match) {
        return match.getStats().stream()
                .filter(s -> !s.isGuest())
                .findFirst()
                .map(TeamStatsMapper.INSTANCE::toDto)
                .orElse(null);
    }
}
