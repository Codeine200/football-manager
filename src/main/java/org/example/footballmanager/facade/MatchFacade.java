package org.example.footballmanager.facade;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.footballmanager.domain.Match;
import org.example.footballmanager.domain.MatchFinish;
import org.example.footballmanager.dto.request.MatchCreateRequestDto;
import org.example.footballmanager.dto.request.MatchFinishRequestDto;
import org.example.footballmanager.dto.response.MatchResponseDto;
import org.example.footballmanager.entity.MatchEntity;
import org.example.footballmanager.mapper.MatchMapper;
import org.example.footballmanager.service.MatchService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchFacade {

    private final MatchService matchService;
    private final MatchMapper matchMapper;

    @Transactional
    public MatchResponseDto createMatch(MatchCreateRequestDto requestDto) {
        Match match = matchMapper.toDomain(requestDto);
        MatchEntity matchSaved = matchService.createMatch(match);
        return matchMapper.toDto(matchSaved);
    }

    @Transactional
    public MatchResponseDto finishMatch(Long id, MatchFinishRequestDto request) {
        MatchFinish matchFinish = matchMapper.toDomain(request);
        return matchMapper.toDto(matchService.finishMatch(id, matchFinish));
    }
}
