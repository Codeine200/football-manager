package org.example.footballmanager.facade;

import lombok.RequiredArgsConstructor;
import org.example.footballmanager.dto.request.TeamRequestDto;
import org.example.footballmanager.dto.response.PageResponse;
import org.example.footballmanager.dto.response.TeamResponseDto;
import org.example.footballmanager.entity.TeamEntity;
import org.example.footballmanager.mapper.PageMapper;
import org.example.footballmanager.mapper.TeamMapper;
import org.example.footballmanager.service.TeamService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamFacade {

    private final TeamService teamService;
    private final TeamMapper teamMapper;
    private final PageMapper pageMapper;

    @Cacheable(value = "teams", key = "#id")
    public TeamResponseDto findById(Long id) {
        return teamMapper.toDto(teamService.findById(id));
    }

    @Cacheable(
            value = "teams-page",
            key = "'p=' + #pageable.pageNumber + " +
                    "'&s=' + #pageable.pageSize + " +
                    "'&sort=' + #pageable.sort"
    )
    public PageResponse<TeamResponseDto> findAll(Pageable pageable) {
        Page<TeamResponseDto> page = teamService
                .findAll(pageable)
                .map(teamMapper::toDto);

        return pageMapper.toDto(page);
    }

    @CacheEvict(value = "teams-page", allEntries = true)
    public TeamResponseDto save(TeamRequestDto requestDto) {
        TeamEntity teamEntity = teamMapper.toEntity(requestDto);
        TeamEntity savedTeamEntity = teamService.save(teamEntity);
        return teamMapper.toDto(savedTeamEntity);
    }

    @Caching(
            put = @CachePut(value = "teams", key = "#id"),
            evict = {
                    @CacheEvict(value = "teams-page", allEntries = true),
                    @CacheEvict(value = "players-page", allEntries = true)
            }
    )
    public TeamResponseDto update(Long id, TeamRequestDto dto) {
        TeamEntity teamEntity = teamService.findById(id);
        teamMapper.updateFromDto(dto, teamEntity);
        return teamMapper.toDto(teamService.save(teamEntity));
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "teams", key = "#id"),
                    @CacheEvict(value = "teams-page", allEntries = true),
                    @CacheEvict(value = "players-page", allEntries = true),
                    @CacheEvict(value = "players", allEntries = true)
            }
    )
    public void deleteById(Long id) {
        teamService.deleteById(id);
    }
}
