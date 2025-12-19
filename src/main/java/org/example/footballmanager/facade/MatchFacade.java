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
        MatchStats stats1 = matchStatsService.create(requestDto.getTeam1());
        MatchStats stats2 = matchStatsService.create(requestDto.getTeam2());
        match.addStats(stats1);
        match.addStats(stats2);

        return matchMapper.toDto(matchService.save(match));
    }
}
