package org.example.footballmanager.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.footballmanager.dto.request.TeamRequestDto;
import org.example.footballmanager.dto.response.PageResponse;
import org.example.footballmanager.dto.response.TeamResponseDto;
import org.example.footballmanager.entity.TeamEntity;
import org.example.footballmanager.mapper.PageMapper;
import org.example.footballmanager.mapper.TeamMapper;
import org.example.footballmanager.service.TeamService;
import org.example.footballmanager.store.TeamFileStorageService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TeamFacade {

    private final TeamService teamService;
    private final TeamMapper teamMapper;
    private final PageMapper pageMapper;
    private final TeamFileStorageService fileStorageService;

    @Cacheable(value = "teams", key = "#id")
    @Transactional(readOnly = true)
    public TeamResponseDto findById(Long id) {
        return teamMapper.toDto(teamService.findById(id));
    }

    @Transactional(readOnly = true)
    @Cacheable(
            value = "teams-page",
            key = "'p=' + #pageable.pageNumber + " +
                    "'&search=' + (#search != null ? #search : '') + " +
                    "'&s=' + #pageable.pageSize + " +
                    "'&sort=' + #pageable.sort"
    )
    public PageResponse<TeamResponseDto> findAll(String search, Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            String searchTerm = search.trim().toLowerCase();
            Page<TeamResponseDto> page = teamService
                    .searchTeams(searchTerm, pageable)
                    .map(teamMapper::toDto);
            return pageMapper.toDto(page);
        }
        Page<TeamResponseDto> page = teamService
                .findAll(pageable)
                .map(teamMapper::toDto);

        return pageMapper.toDto(page);
    }

    @CacheEvict(value = "teams-page", allEntries = true)
    public TeamResponseDto save(TeamRequestDto requestDto, MultipartFile file) {
        TeamEntity teamEntity = teamMapper.toEntity(requestDto);
        TeamEntity savedTeamEntity = teamService.save(teamEntity, file);
        return teamMapper.toDto(savedTeamEntity);
    }

    @Caching(
            put = @CachePut(value = "teams", key = "#id"),
            evict = {
                    @CacheEvict(value = "teams-page", allEntries = true),
                    @CacheEvict(value = "players-page", allEntries = true)
            }
    )
    public TeamResponseDto update(Long id, TeamRequestDto dto, MultipartFile file) {
        TeamEntity teamEntity = teamService.findById(id);
        teamMapper.updateFromDto(dto, teamEntity);
        return teamMapper.toDto(teamService.save(teamEntity, file));
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "teams", key = "#id"),
                    @CacheEvict(value = "teams-page", allEntries = true),
                    @CacheEvict(value = "players-page", allEntries = true),
                    @CacheEvict(value = "players", allEntries = true),
                    @CacheEvict(value = "matches", allEntries = true),
                    @CacheEvict(value = "matches-page", allEntries = true),
            }
    )
    public void deleteById(Long id) {
        TeamEntity teamEntity = teamService.deleteById(id);
        if (teamEntity.getLogo() != null && !teamEntity.getLogo().isBlank()) {
            try {
                fileStorageService.delete(teamEntity.getLogo());
            } catch (IOException e) {
                log.error("Failed to delete logo file '{}' for team with id {}",
                        teamEntity.getLogo(),
                        teamEntity.getId(),
                        e);
            }
        }
    }
}
