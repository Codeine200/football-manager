package org.example.footballmanager.mapper;

import org.example.footballmanager.domain.Match;
import org.example.footballmanager.domain.MatchFinish;
import org.example.footballmanager.domain.MatchTeamResult;
import org.example.footballmanager.domain.Team;
import org.example.footballmanager.domain.TeamId;
import org.example.footballmanager.dto.request.MatchCreateRequestDto;
import org.example.footballmanager.dto.request.MatchFinishRequestDto;
import org.example.footballmanager.dto.response.MatchResponseDto;
import org.example.footballmanager.entity.MatchEntity;
import org.example.footballmanager.entity.MatchStatsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


@Mapper(componentModel = "spring", uses = {TeamStatsMapper.class})
public interface MatchMapper {

    @Mapping(target = "id", ignore = true)
    MatchEntity toEntity(Match matchCreate);

    default MatchFinish toDomain(MatchFinishRequestDto dto) {
        return new MatchFinish(
                new MatchTeamResult(new TeamId(dto.team1().teamId()), dto.team1().goals()),
                new MatchTeamResult(new TeamId(dto.team2().teamId()), dto.team2().goals())
        );
    }

    @Mapping(target = "team1", expression = "java(mapTeam(dto.getTeam1()))")
    @Mapping(target = "team2", expression = "java(mapTeam(dto.getTeam2()))")
    Match toDomain(MatchCreateRequestDto dto);

    default Team mapTeam(MatchCreateRequestDto.TeamDto dto) {
        return new Team(
                new TeamId(dto.getId()),
                dto.isGuest()
        );
    }

    @Mapping(target = "team1", source = "match", qualifiedByName = "mapTeam1")
    @Mapping(target = "team2", source = "match", qualifiedByName = "mapTeam2")
    MatchResponseDto toDto(MatchEntity match);

    @Named("mapTeam1")
    default MatchResponseDto.TeamStatsDto mapTeam1(MatchEntity matchEntity) {
        return matchEntity.getStats().stream()
                .filter(MatchStatsEntity::isGuest)
                .findFirst()
                .map(TeamStatsMapper.INSTANCE::toDto)
                .orElse(null);
    }

    @Named("mapTeam2")
    default  MatchResponseDto.TeamStatsDto mapTeam2(MatchEntity matchEntity) {
        return matchEntity.getStats().stream()
                .filter(s -> !s.isGuest())
                .findFirst()
                .map(TeamStatsMapper.INSTANCE::toDto)
                .orElse(null);
    }
}
