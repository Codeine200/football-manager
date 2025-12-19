package org.example.footballmanager.service;

import lombok.RequiredArgsConstructor;
import org.example.footballmanager.entity.Match;
import org.example.footballmanager.entity.MatchStats;
import org.example.footballmanager.entity.Team;
import org.example.footballmanager.model.TeamTournamentStats;
import org.example.footballmanager.repository.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;

    public Match save(Match match) {
        return matchRepository.save(match);
    }

    public Set<TeamTournamentStats> getTeamStatsBySeason(String season) {
        List<Match> matches = matchRepository.findAllBySeason(season);
        if (matches.isEmpty()) {
            return Collections.emptySet();
        }

        Map<Team, TeamTournamentStats> teamMap = new HashMap<>();
        matches.forEach(match -> {
            List<MatchStats> stats = match.getStats();
            if (stats.size() != 2) {
                throw new IllegalStateException(
                        "Invalid match state: expected exactly 2 team stats for match id="
                                + match.getId()
                                + ", but found "
                                + stats.size()
                );
            }

            MatchStats teamStats1 = stats.getFirst();
            MatchStats teamStats2 = stats.getLast();

            TeamTournamentStats teamTournamentStats1 =  teamMap.getOrDefault(teamStats1.getTeam(), new TeamTournamentStats());
            TeamTournamentStats teamTournamentStats2 =  teamMap.getOrDefault(teamStats2.getTeam(), new TeamTournamentStats());

            teamTournamentStats1.setSeason(match.getSeason());
            teamTournamentStats1.setPlayed(teamTournamentStats1.getPlayed() + 1);
            teamTournamentStats1.setWins((teamStats1.getIsWinner()) ? teamTournamentStats1.getWins() + 1 : teamTournamentStats1.getWins());
            teamTournamentStats1.setDraws((!teamStats1.getIsWinner() && !teamStats2.getIsWinner()) ? teamTournamentStats1.getDraws() + 1 : teamTournamentStats1.getDraws());
            teamTournamentStats1.setLosses((!teamStats1.getIsWinner()) ? teamTournamentStats1.getLosses() + 1 : teamTournamentStats1.getLosses());
            teamTournamentStats1.setGoalsFor(teamTournamentStats1.getGoalsFor() + teamStats1.getGoals());
            teamTournamentStats1.setGoalsAgainst(teamTournamentStats1.getGoalsAgainst() + teamStats2.getGoals());
            teamTournamentStats1.setGoalDifference(teamTournamentStats1.getGoalsFor() - teamTournamentStats1.getGoalsAgainst());
            teamTournamentStats1.setPoints(teamTournamentStats1.getPoints() + teamStats1.getScore());
            teamMap.put(teamStats1.getTeam(), teamTournamentStats1);

            teamTournamentStats2.setSeason(match.getSeason());
            teamTournamentStats2.setPlayed(teamTournamentStats2.getPlayed() + 1);
            teamTournamentStats2.setWins((teamStats2.getIsWinner()) ? teamTournamentStats2.getWins() + 1 : teamTournamentStats2.getWins());
            teamTournamentStats2.setDraws((!teamStats1.getIsWinner() && !teamStats2.getIsWinner()) ? teamTournamentStats2.getDraws() + 1 : teamTournamentStats2.getDraws());
            teamTournamentStats2.setLosses((!teamStats2.getIsWinner()) ? teamTournamentStats2.getLosses() + 1 : teamTournamentStats2.getLosses());
            teamTournamentStats2.setGoalsFor(teamTournamentStats2.getGoalsFor() + teamStats2.getGoals());
            teamTournamentStats2.setGoalsAgainst(teamTournamentStats2.getGoalsAgainst() + teamStats1.getGoals());
            teamTournamentStats2.setGoalDifference(teamTournamentStats2.getGoalsFor() - teamTournamentStats2.getGoalsAgainst());
            teamTournamentStats2.setPoints(teamTournamentStats2.getPoints() + teamStats2.getScore());
            teamMap.put(teamStats2.getTeam(), teamTournamentStats2);
        });

        return new HashSet<>(teamMap.values());
    }
}
