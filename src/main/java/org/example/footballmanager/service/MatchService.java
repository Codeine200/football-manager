package org.example.footballmanager.service;

import lombok.RequiredArgsConstructor;
import org.example.footballmanager.dto.request.MatchFinishRequestDto;
import org.example.footballmanager.dto.request.MatchFinishRequestDto.TeamDto;
import org.example.footballmanager.entity.Match;
import org.example.footballmanager.entity.MatchStats;
import org.example.footballmanager.entity.Team;
import org.example.footballmanager.exception.MatchNotFoundException;
import org.example.footballmanager.exception.TeamNotFoundException;
import org.example.footballmanager.model.TeamTournamentStats;
import org.example.footballmanager.repository.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final TeamService teamService;

    public Match findById(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new MatchNotFoundException(id));
    }

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
            teamTournamentStats1.setTeam(teamStats1.getTeam());
            TeamTournamentStats teamTournamentStats2 =  teamMap.getOrDefault(teamStats2.getTeam(), new TeamTournamentStats());
            teamTournamentStats2.setTeam(teamStats2.getTeam());

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

    public Match finishMatch(Long id, MatchFinishRequestDto request) {
        Match match = findById(id);
        Team team1 = match.getStats().getFirst().getTeam();
        Team team2 = match.getStats().getLast().getTeam();
        if (!isCorrectTeam(team1, request)) {
            throw new IllegalStateException("Invalid team IDs were provided for an existing match.");
        }
        if (!isCorrectTeam(team2, request)) {
            throw new IllegalStateException("Invalid team IDs were provided for an existing match.");

        }

        List<MatchStats> matchStats = getMatchStats(request, match);
        match.setFinished(true);
        match.setStats(matchStats);
        return matchRepository.save(match);
    }

    private List<MatchStats> getMatchStats(MatchFinishRequestDto request, Match match) {
        MatchStats.MatchStatsBuilder matchStatsBuilder1 = MatchStats.builder();
        matchStatsBuilder1.match(match);
        MatchStats.MatchStatsBuilder matchStatsBuilder2 = MatchStats.builder();
        matchStatsBuilder2.match(match);
        if (request.getTeam1().getGoals() == request.getTeam2().getGoals()) {
            matchStatsBuilder1.isWinner(false);
            matchStatsBuilder1.score(1);
            matchStatsBuilder1.goals(request.getTeam1().getGoals());
            matchStatsBuilder1.team(teamService.findById(request.getTeam1().getTeamId()));
            matchStatsBuilder1.isGuest(isGuest(request.getTeam1(), match));

            matchStatsBuilder2.isWinner(false);
            matchStatsBuilder2.score(1);
            matchStatsBuilder2.goals(request.getTeam2().getGoals());
            matchStatsBuilder2.team(teamService.findById(request.getTeam2().getTeamId()));
            matchStatsBuilder2.isGuest(isGuest(request.getTeam2(), match));

        } else if (request.getTeam1().getGoals() > request.getTeam2().getGoals()) {
            matchStatsBuilder1.isWinner(true);
            matchStatsBuilder1.team(teamService.findById(request.getTeam1().getTeamId()));
            matchStatsBuilder1.goals(request.getTeam1().getGoals());
            matchStatsBuilder1.score(3);
            matchStatsBuilder1.isGuest(isGuest(request.getTeam1(), match));

            matchStatsBuilder2.isWinner(false);
            matchStatsBuilder2.team(teamService.findById(request.getTeam2().getTeamId()));
            matchStatsBuilder2.goals(request.getTeam2().getGoals());
            matchStatsBuilder2.score(0);
            matchStatsBuilder2.isGuest(isGuest(request.getTeam2(), match));
        } else {
            matchStatsBuilder1.isWinner(false);
            matchStatsBuilder1.team(teamService.findById(request.getTeam1().getTeamId()));
            matchStatsBuilder1.goals(request.getTeam1().getGoals());
            matchStatsBuilder1.score(0);
            matchStatsBuilder1.isGuest(isGuest(request.getTeam1(), match));

            matchStatsBuilder2.isWinner(true);
            matchStatsBuilder2.team(teamService.findById(request.getTeam2().getTeamId()));
            matchStatsBuilder2.goals(request.getTeam2().getGoals());
            matchStatsBuilder2.score(3);
            matchStatsBuilder2.isGuest(isGuest(request.getTeam2(), match));
        }

        List<MatchStats> matchStats = new ArrayList<>();
        matchStats.add(matchStatsBuilder1.build());
        matchStats.add(matchStatsBuilder2.build());
        return matchStats;
    }

    private boolean isGuest(TeamDto team1, Match match) {
        return match.getStats().stream().anyMatch(s -> s.getTeam().getId().equals(team1.getTeamId()) && s.isGuest());
    }

    private boolean isCorrectTeam(Team team, MatchFinishRequestDto request) {
        List<Long> ids = List.of(request.getTeam1().getTeamId(), request.getTeam2().getTeamId());
        return ids.contains(team.getId());
    }
}
