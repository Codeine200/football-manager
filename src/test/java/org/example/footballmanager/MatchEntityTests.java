package org.example.footballmanager;

import org.example.footballmanager.domain.TeamTournamentStats;
import org.example.footballmanager.entity.MatchEntity;
import org.example.footballmanager.entity.MatchStatsEntity;
import org.example.footballmanager.entity.TeamEntity;
import org.example.footballmanager.repository.MatchRepository;
import org.example.footballmanager.service.MatchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MatchEntityTests {

    @Mock
    MatchRepository matchRepository;

    @InjectMocks
    MatchService matchService;

    @Test
    void shouldReturnFullStatisticForAllTeamBySeason() {
        String season = "2024";

        MatchEntity matchEntity = new MatchEntity();
        matchEntity.setSeason(season);
        matchEntity.setMatchDate(LocalDate.now());

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

        when(matchRepository.findAllBySeason(season))
                .thenReturn(List.of(matchEntity));

        Set<TeamTournamentStats> result =
                matchService.getTeamStatsBySeason(season);

        TeamTournamentStats teamAStats = result.stream()
                .filter(s -> s.getTeamEntity().equals(teamEntityA))
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

        verify(matchRepository).findAllBySeason(season);
    }
}
