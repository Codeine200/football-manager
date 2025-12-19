package org.example.footballmanager.service;

import lombok.RequiredArgsConstructor;
import org.example.footballmanager.dto.request.MatchCreateRequestDto.TeamDto;
import org.example.footballmanager.entity.MatchStats;
import org.example.footballmanager.entity.Team;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchStatsService {

    private final TeamService teamService;

    public MatchStats create(TeamDto teamDto) {
        Long teamId = teamDto.getId();
        boolean teamIsGuest = teamDto.isGuest();
        Team team = teamService.findById(teamId);

        return MatchStats.builder()
                .team(team)
                .isGuest(teamIsGuest)
                .build();
    }
}
