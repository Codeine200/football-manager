package org.example.footballmanager;

import org.example.footballmanager.domain.TeamTournamentStats;
import org.example.footballmanager.dto.response.PageResponse;
import org.example.footballmanager.entity.MatchEntity;
import org.example.footballmanager.entity.MatchStatsEntity;
import org.example.footballmanager.entity.TeamEntity;
import org.example.footballmanager.mapper.TeamMapper;
import org.example.footballmanager.repository.MatchRepository;
import org.example.footballmanager.service.MatchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MatchEntityTests {

    @Mock
    MatchRepository matchRepository;

    @Mock
    TeamMapper teamMapper;

    @InjectMocks
    MatchService matchService;

    @Test
    void shouldReturnFullStatisticForAllTeamBySeason() {
        Integer season = 2024;

        MatchEntity matchEntity = new MatchEntity();
        matchEntity.setSeason(season);
        matchEntity.setMatchDateTime(LocalDateTime.now());

        TeamEntity teamEntityA = new TeamEntity();
        teamEntityA.setId(1L);
        teamEntityA.setName("Team A");

        TeamEntity teamEntityB = new TeamEntity();
        teamEntityB.setId(2L);
        teamEntityB.setName("Team B");

        MatchStatsEntity stats1 = MatchStatsEntity.builder()
                .match(matchEntity)
                .team(teamEntityA)
                .goals(2)
                .score(3)
                .isWinner(true)
                .isGuest(false)
                .build();

        MatchStatsEntity stats2 = MatchStatsEntity.builder()
                .match(matchEntity)
                .team(teamEntityB)
                .goals(1)
                .score(0)
                .isWinner(false)
                .isGuest(true)
                .build();

        matchEntity.addStats(stats1);
        matchEntity.addStats(stats2);

        Pageable pageable = PageRequest.of(0, 10);

        when(matchRepository.findDistinctSeasons(pageable))
                .thenReturn(new PageImpl<>(List.of(season), pageable, 1));

        when(matchRepository.findAllBySeasonAndIsFinishedTrue(season))
                .thenReturn(List.of(matchEntity));

        when(teamMapper.toDomain(teamEntityA))
                .thenReturn(new org.example.footballmanager.domain.TeamData(teamEntityA.getId(), teamEntityA.getName(), null));

        when(teamMapper.toDomain(teamEntityB))
                .thenReturn(new org.example.footballmanager.domain.TeamData(teamEntityB.getId(), teamEntityB.getName(), null));

        PageResponse<Map<Integer, List<TeamTournamentStats>>> pageResponse =
                matchService.getTeamStatsBySeason(pageable);

        Map<Integer, List<TeamTournamentStats>> resultMap = pageResponse.getItems().get(0);

        List<TeamTournamentStats> seasonStats = resultMap.get(2024);

        TeamTournamentStats teamAStats = seasonStats.stream()
                .filter(s -> s.getTeam().getId().equals(teamEntityA.getId()))
                .findFirst()
                .orElseThrow();

        assertEquals(1, teamAStats.getPlayed());
        assertEquals(1, teamAStats.getWins());
        assertEquals(0, teamAStats.getDraws());
        assertEquals(0, teamAStats.getLosses());
        assertEquals(2, teamAStats.getGoalsFor());
        assertEquals(1, teamAStats.getGoalsAgainst());
        assertEquals(1, teamAStats.getGoalDifference());
        assertEquals(3, teamAStats.getPoints());

        verify(matchRepository).findAllBySeasonAndIsFinishedTrue(season);
    }
}
