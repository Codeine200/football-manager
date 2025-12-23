package org.example.footballmanager;

import org.example.footballmanager.entity.Match;
import org.example.footballmanager.entity.MatchStats;
import org.example.footballmanager.entity.Team;
import org.example.footballmanager.model.TeamTournamentStats;
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
public class MatchTests {

    @Mock
    MatchRepository matchRepository;

    @InjectMocks
    MatchService matchService;

    @Test
    void shouldReturnFullStatisticForAllTeamBySeason() {
        String season = "2024";

        Match match = new Match();
        match.setSeason(season);
        match.setMatchDate(LocalDate.now());

        Team teamA = new Team();
        teamA.setId(1L);
        teamA.setName("Team A");

        Team teamB = new Team();
        teamB.setId(2L);
        teamB.setName("Team 2");

        MatchStats stats1 = MatchStats.builder()
                .match(match)
                .team(teamA)
                .goals(2)
                .score(3)
                .isWinner(true)
                .isGuest(false)
                .build();

        MatchStats stats2 = MatchStats.builder()
                .match(match)
                .team(teamB)
                .goals(1)
                .score(0)
                .isWinner(false)
                .isGuest(true)
                .build();


        match.addStats(stats1);
        match.addStats(stats2);


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
