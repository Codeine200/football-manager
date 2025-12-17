package org.example.footballmanager.facade;

import lombok.RequiredArgsConstructor;
import org.example.footballmanager.dto.request.MatchCreateRequestDto;
import org.example.footballmanager.dto.response.MatchResponseDto;
import org.example.footballmanager.entity.Match;
import org.example.footballmanager.entity.MatchStats;
import org.example.footballmanager.mapper.MatchMapper;
import org.example.footballmanager.service.MatchService;
import org.example.footballmanager.service.MatchStatsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchFacade {

    private final MatchService matchService;
    private final MatchStatsService matchStatsService;
    private final MatchMapper matchMapper;

    public MatchResponseDto createMatch(MatchCreateRequestDto requestDto) {
        Match match = matchMapper.toEntity(requestDto);
        MatchStats team1 = matchStatsService.create(requestDto.getTeam1().getId(), requestDto.getTeam1().isGuest());
        MatchStats team2 = matchStatsService.create(requestDto.getTeam2().getId(), requestDto.getTeam2().isGuest());
        team1.setMatch(match);
        match.getStats().add(team1);
        team2.setMatch(match);
        match.getStats().add(team2);

        return matchMapper.toDto(matchService.save(match));
    }
}
