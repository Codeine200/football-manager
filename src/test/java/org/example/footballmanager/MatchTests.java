package org.example.footballmanager;

import org.example.footballmanager.entity.MatchStats;
import org.example.footballmanager.entity.Team;
import org.example.footballmanager.entity.Match;
import org.example.footballmanager.model.TeamTournamentStats;
import org.example.footballmanager.repository.MatchRepository;
import org.example.footballmanager.service.MatchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MatchTests {

    @Mock
    MatchRepository matchRepository;

    @InjectMocks
    MatchService matchService;

    @Test
    void shouldReturnFullStatisticForAllTeamBySeason() {
        String season = "2024";

        Team teamA = Team.builder()
                .id(1L)
                .name("Team A")
                .build();
        Team teamB = Team.builder()
                .id(2L)
                .name("Team B")
                .build();

        Match match = new Match();
        match.setId(100L);
        match.setSeason(season);

        MatchStats statsA = new MatchStats();
        statsA.setTeam(teamA);
        statsA.setGoals(2);
        statsA.setIsWinner(true);
        statsA.setScore(3);

        MatchStats statsB = new MatchStats();
        statsB.setTeam(teamB);
        statsB.setGoals(1);
        statsB.setIsWinner(false);
        statsB.setScore(0);

        match.setStats(List.of(statsA, statsB));

        when(matchRepository.findAllBySeason(season))
                .thenReturn(List.of(match));

        Set<TeamTournamentStats> result =
                matchService.getTeamStatsBySeason(season);

        TeamTournamentStats teamAStats = result.stream()
                .filter(s -> s.getTeam().equals(teamA))
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
