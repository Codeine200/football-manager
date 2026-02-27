package org.example.footballmanager.facade;

import lombok.RequiredArgsConstructor;
import org.example.footballmanager.domain.Player;
import org.example.footballmanager.domain.TeamId;
import org.example.footballmanager.dto.request.PlayerAssignRequestDto;
import org.example.footballmanager.dto.request.PlayerRequestDto;
import org.example.footballmanager.dto.response.PageResponse;
import org.example.footballmanager.dto.response.PlayerResponseDto;
import org.example.footballmanager.entity.PlayerEntity;
import org.example.footballmanager.entity.TeamEntity;
import org.example.footballmanager.mapper.PageMapper;
import org.example.footballmanager.mapper.PlayerMapper;
import org.example.footballmanager.service.PlayerService;
import org.example.footballmanager.service.TeamService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class PlayerFacade {

    private final PlayerService playerService;
    private final TeamService teamService;
    private final PlayerMapper playerMapper;
    private final PageMapper pageMapper;

    @Transactional(readOnly = true)
    @Cacheable(value = "players", key = "#id")
    public PlayerResponseDto findById(Long id) {
        PlayerEntity playerEntity = playerService.findById(id);
        return playerMapper.toDto(playerEntity);
    }

    @Transactional(readOnly = true)
    @Cacheable(
            value = "players-page",
            key = "'team=' + #teamId + " +
                    "'&p=' + #pageable.pageNumber + " +
                    "'&s=' + #pageable.pageSize + " +
                    "'&sort=' + #pageable.sort"
    )
    public PageResponse<PlayerResponseDto> findAllByTeamId(Long teamId, Pageable pageable) {
        if (teamId == null) {
            Page<PlayerResponseDto> page = playerService
                    .findAll(pageable)
                    .map(playerMapper::toDto);

            return pageMapper.toDto(page);
        }

        Page<PlayerResponseDto> page = playerService
                .findAllByTeamId(teamId, pageable)
                .map(playerMapper::toDto);

        return pageMapper.toDto(page);
    }

    @Transactional
    @CacheEvict(value = "players-page", allEntries = true)
    public PlayerResponseDto create(PlayerRequestDto dto, MultipartFile file) {
        Player player = playerMapper.toDomain(dto);
        PlayerEntity saved = playerService.create(player, file);
        return playerMapper.toDto(saved);
    }

    @Transactional
    @CachePut(value = "players", key = "#playerId")
    @CacheEvict(value = "players-page", allEntries = true)
    public PlayerResponseDto update(Long playerId, PlayerRequestDto dto, MultipartFile file) {
        PlayerEntity playerEntity = playerService.findById(playerId);

        TeamEntity teamEntity = null;
        if (dto.teamId() != null) {
            teamEntity = teamService.getReferenceById(dto.teamId());
        }

        playerMapper.updateFromDto(dto, playerEntity);
        playerEntity.setTeam(teamEntity);
        PlayerEntity saved = playerService.save(playerEntity, file);
        return playerMapper.toDto(saved);
    }

    @Transactional
    @CachePut(value = "players", key = "#playerId")
    @CacheEvict(value = "players-page", allEntries = true)
    public PlayerResponseDto assign(Long playerId, PlayerAssignRequestDto dto) {
        PlayerEntity saved = playerService.assign(playerId, new TeamId(dto.teamId()));
        return playerMapper.toDto(saved);
    }

    @CachePut(value = "players", key = "#id")
    @CacheEvict(value = "players-page", allEntries = true)
    public void deleteById(Long id) {
        playerService.deleteById(id);
    }
}
