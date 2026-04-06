package org.example.footballmanager.mapper;

import org.example.footballmanager.domain.Match;
import org.example.footballmanager.domain.MatchFinish;
import org.example.footballmanager.domain.MatchFullInfo;
import org.example.footballmanager.domain.MatchTeamResult;
import org.example.footballmanager.domain.Team;
import org.example.footballmanager.domain.TeamFullInfo;
import org.example.footballmanager.domain.TeamId;
import org.example.footballmanager.dto.request.MatchFinishRequestDto;
import org.example.footballmanager.dto.request.MatchRequestDto;
import org.example.footballmanager.dto.request.MatchUpdateRequestDto;
import org.example.footballmanager.dto.response.MatchResponseDto;
import org.example.footballmanager.entity.MatchEntity;
import org.example.footballmanager.entity.MatchStatsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {TeamStatsMapper.class, TeamFileStorageMapper.class})
public abstract class MatchMapper {

    @Autowired
    protected TeamStatsMapper teamStatsMapper;

    @Mapping(target = "id", ignore = true)
    public abstract MatchEntity toEntity(Match matchCreate);

    public MatchFinish toDomain(MatchFinishRequestDto dto) {
        return new MatchFinish(
                new MatchTeamResult(new TeamId(dto.team1().teamId()), dto.team1().goals()),
                new MatchTeamResult(new TeamId(dto.team2().teamId()), dto.team2().goals())
        );
    }

    @Mapping(target = "team1", expression = "java(mapTeam(dto.getTeam1()))")
    @Mapping(target = "team2", expression = "java(mapTeam(dto.getTeam2()))")
    public abstract Match toDomain(MatchRequestDto dto);

    protected Team mapTeam(MatchRequestDto.TeamDto dto) {
        return new Team(
                new TeamId(dto.getId()),
                dto.isGuest()
        );
    }

    @Mapping(target = "team1", source = "match", qualifiedByName = "mapTeam1")
    @Mapping(target = "team2", source = "match", qualifiedByName = "mapTeam2")
    public abstract MatchResponseDto toDto(MatchEntity match);

    @Mapping(target = "team1", source = "team1")
    @Mapping(target = "team2", source = "team2")
    public abstract MatchFullInfo toDomain(MatchUpdateRequestDto dto);

    @Mapping(target = "teamId", source = "id")
    public abstract TeamFullInfo toDomain(MatchUpdateRequestDto.TeamDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stats", ignore = true)
    public abstract void updateFromMatchFullInfo(MatchFullInfo matchFullInfo,
                                                 @MappingTarget MatchEntity entity);

    @Named("mapTeam1")
    protected MatchResponseDto.TeamStatsDto mapTeam1(MatchEntity matchEntity) {
        return matchEntity.getStats().stream()
                .filter(MatchStatsEntity::isGuest)
                .findFirst()
                .map(teamStatsMapper::toDto)
                .orElse(null);
    }

    @Named("mapTeam2")
    protected MatchResponseDto.TeamStatsDto mapTeam2(MatchEntity matchEntity) {
        return matchEntity.getStats().stream()
                .filter(s -> !s.isGuest())
                .findFirst()
                .map(teamStatsMapper::toDto)
                .orElse(null);
    }

    protected TeamId map(Long value) {
        return value == null ? null : new TeamId(value);
    }
}