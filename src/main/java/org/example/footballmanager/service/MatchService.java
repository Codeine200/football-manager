package org.example.footballmanager.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.footballmanager.domain.Match;
import org.example.footballmanager.domain.MatchFinish;
import org.example.footballmanager.domain.MatchTeamResult;
import org.example.footballmanager.domain.Team;
import org.example.footballmanager.domain.TeamTournamentStats;
import org.example.footballmanager.entity.MatchEntity;
import org.example.footballmanager.entity.MatchStatsEntity;
import org.example.footballmanager.entity.TeamEntity;
import org.example.footballmanager.exception.MatchNotFoundException;
import org.example.footballmanager.mapper.MatchMapper;
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
    private final MatchMapper matchMapper;
    private final TeamService teamService;

    public MatchEntity findById(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new MatchNotFoundException(id));
    }

    public MatchEntity createMatch(Match matchCreate) {
        MatchEntity match = matchMapper.toEntity(matchCreate);
        MatchStatsEntity stats1 = createStats(matchCreate.getTeam1());
        MatchStatsEntity stats2 = createStats(matchCreate.getTeam2());
        match.addStats(stats1);
        match.addStats(stats2);

        return save(match);
    }

    public MatchEntity save(MatchEntity matchEntity) {
        return matchRepository.save(matchEntity);
    }

    public Set<TeamTournamentStats> getTeamStatsBySeason(String season) {
        List<MatchEntity> matchEntities = matchRepository.findAllBySeason(season);
        if (matchEntities.isEmpty()) {
            return Collections.emptySet();
        }

        Map<TeamEntity, TeamTournamentStats> teamMap = new HashMap<>();
        matchEntities.forEach(match -> {
            List<MatchStatsEntity> stats = match.getStats();
            if (stats.size() != 2) {
                throw new IllegalStateException(
                        "Invalid match state: expected exactly 2 team stats for match id="
                                + match.getId()
                                + ", but found "
                                + stats.size()
                );
            }

            MatchStatsEntity teamStats1 = stats.getFirst();
            MatchStatsEntity teamStats2 = stats.getLast();

            TeamTournamentStats teamTournamentStats1 =  teamMap.getOrDefault(teamStats1.getTeam(), new TeamTournamentStats());
            teamTournamentStats1.setTeamEntity(teamStats1.getTeam());
            TeamTournamentStats teamTournamentStats2 =  teamMap.getOrDefault(teamStats2.getTeam(), new TeamTournamentStats());
            teamTournamentStats2.setTeamEntity(teamStats2.getTeam());

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

    @Transactional
    public MatchEntity finishMatch(Long id, MatchFinish matchFinish) {
        MatchEntity matchEntity = findById(id);
        TeamEntity teamEntity1 = matchEntity.getStats().getFirst().getTeam();
        TeamEntity teamEntity2 = matchEntity.getStats().getLast().getTeam();
        if (!isCorrectTeam(teamEntity1, matchFinish)) {
            throw new IllegalStateException("Invalid team IDs were provided for an existing match.");
        }
        if (!isCorrectTeam(teamEntity2, matchFinish)) {
            throw new IllegalStateException("Invalid team IDs were provided for an existing match.");
        }

        updateMatchStats(matchFinish, matchEntity);
        matchEntity.setFinished(true);
        return matchRepository.save(matchEntity);
    }

    private void updateMatchStats(MatchFinish matchFinish, MatchEntity matchEntity) {
        for (MatchStatsEntity stats : matchEntity.getStats()) {
            MatchTeamResult team1 = matchFinish.getTeam1();
            MatchTeamResult team2 = matchFinish.getTeam2();
            if (stats.getTeam().getId().equals(team1.getTeamId().id())) {
                stats.setGoals(team1.getGoals());
                stats.setScore(calculateScore(team1.getGoals(), team2.getGoals()));
                stats.setIsWinner(team1.getGoals() > team2.getGoals());
                stats.setGuest(isGuest(team1, matchEntity));
            } else if (stats.getTeam().getId().equals(team2.getTeamId().id())) {
                stats.setGoals(team2.getGoals());
                stats.setScore(calculateScore(team2.getGoals(), team1.getGoals()));
                stats.setIsWinner(team2.getGoals() > team1.getGoals());
                stats.setGuest(isGuest(team2, matchEntity));
            }
        }
    }

    private int calculateScore(int goalsFor, int goalsAgainst) {
        if (goalsFor > goalsAgainst) {
            return 3;
        } else if (goalsFor == goalsAgainst) {
            return 1;
        } else {
            return 0;
        }
    }

    private boolean isGuest(MatchTeamResult team, MatchEntity matchEntity) {
        return matchEntity.getStats().stream().anyMatch(s -> s.getTeam().getId().equals(team.getTeamId().id()) && s.isGuest());
    }

    private boolean isCorrectTeam(TeamEntity teamEntity, MatchFinish request) {
        List<Long> ids = List.of(request.getTeam1().getTeamId().id(), request.getTeam2().getTeamId().id());
        return ids.contains(teamEntity.getId());
    }

    private MatchStatsEntity createStats(Team team) {
        Long teamId = team.getTeamId().id();
        boolean teamIsGuest = team.isGuest();
        TeamEntity teamEntity = teamService.findById(teamId);

        return MatchStatsEntity.builder()
                .team(teamEntity)
                .isGuest(teamIsGuest)
                .build();
    }
}
