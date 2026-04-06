package org.example.footballmanager.facade;

import lombok.RequiredArgsConstructor;
import org.example.footballmanager.domain.Match;
import org.example.footballmanager.domain.MatchFinish;
import org.example.footballmanager.domain.MatchFullInfo;
import org.example.footballmanager.domain.TeamTournamentStats;
import org.example.footballmanager.dto.request.MatchFinishRequestDto;
import org.example.footballmanager.dto.request.MatchRequestDto;
import org.example.footballmanager.dto.request.MatchUpdateRequestDto;
import org.example.footballmanager.dto.response.MatchResponseDto;
import org.example.footballmanager.dto.response.PageResponse;
import org.example.footballmanager.entity.MatchEntity;
import org.example.footballmanager.mapper.MatchMapper;
import org.example.footballmanager.mapper.PageMapper;
import org.example.footballmanager.service.MatchService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MatchFacade {

    private final MatchService matchService;
    private final MatchMapper matchMapper;
    private final PageMapper pageMapper;

    @Cacheable(value = "matches", key = "#id")
    public MatchResponseDto findById(Long id) {
        return matchMapper.toDto(matchService.findById(id));
    }

    @Transactional(readOnly = true)
    @Cacheable(
            value = "season",
            key = "'p=' + #pageable.pageNumber + " +
                    "'&s=' + #pageable.pageSize + " +
                    "'&sort=' + #pageable.sort"
    )
    public PageResponse<Map<Integer, List<TeamTournamentStats>>> getTeamStatsBySeason(Pageable pageable) {
        return matchService.getTeamStatsBySeason(pageable);
    }

    @Transactional(readOnly = true)
    @Cacheable(
            value = "matches-page",
            key = "'p=' + #pageable.pageNumber + " +
                    "'&s=' + #pageable.pageSize + " +
                    "'&search=' + (#search != null ? #search : '') + " +
                    "'&sort=' + #pageable.sort + " +
                    "'&isFinished=' + #isFinished"
    )
    public PageResponse<MatchResponseDto> searchMatches(String search, Boolean isFinished, Pageable pageable) {
        Page<MatchResponseDto> page = matchService
                .searchMatches(search, isFinished, pageable)
                .map(matchMapper::toDto);

        return pageMapper.toDto(page);
    }

    @Transactional
    @CacheEvict(value = "matches-page", allEntries = true)
    public MatchResponseDto createMatch(MatchRequestDto requestDto) {
        Match match = matchMapper.toDomain(requestDto);
        MatchEntity matchSaved = matchService.createMatch(match);
        return matchMapper.toDto(matchSaved);
    }

    @Transactional
    @Caching(
            put = @CachePut(value = "matches", key = "#id"),
            evict = {
                    @CacheEvict(value = "matches-page", allEntries = true),
                    @CacheEvict(value = "season", allEntries = true),
            }
    )
    public MatchResponseDto finishMatch(Long id, MatchFinishRequestDto request) {
        MatchFinish matchFinish = matchMapper.toDomain(request);
        return matchMapper.toDto(matchService.finishMatch(id, matchFinish));
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "matches", key = "#id"),
                    @CacheEvict(value = "matches-page", allEntries = true),
                    @CacheEvict(value = "season", allEntries = true),
            }
    )
    public void deleteById(Long id) {
        matchService.deleteById(id);
    }

    @Caching(
            put = @CachePut(value = "matches", key = "#id"),
            evict = {
                    @CacheEvict(value = "matches-page", allEntries = true),
                    @CacheEvict(value = "season", allEntries = true),
            }
    )
    public MatchResponseDto update(Long id, MatchUpdateRequestDto dto) {
        MatchFullInfo newMatch = matchMapper.toDomain(dto);
        return matchMapper.toDto(matchService.updateMatch(id, newMatch));
    }
}