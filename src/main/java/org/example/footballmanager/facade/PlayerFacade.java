package org.example.footballmanager.facade;

import jakarta.transaction.Transactional;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerFacade {

    private final PlayerService playerService;
    private final TeamService teamService;
    private final PlayerMapper playerMapper;
    private final PageMapper pageMapper;

    @Transactional
    public PlayerResponseDto findById(Long id) {
        PlayerEntity playerEntity = playerService.findById(id);
        return playerMapper.toDto(playerEntity);
    }

    @Transactional
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
    public PlayerResponseDto create(PlayerRequestDto dto) {
        Player player = playerMapper.toDomain(dto);
        PlayerEntity saved = playerService.create(player);
        return playerMapper.toDto(saved);
    }

    @Transactional
    public PlayerResponseDto update(Long playerId, PlayerRequestDto dto) {
        PlayerEntity playerEntity = playerService.findById(playerId);

        TeamEntity teamEntity = null;
        if (dto.teamId() != null) {
            teamEntity = teamService.findById(dto.teamId());
        }

        playerMapper.updateFromDto(dto, playerEntity);
        playerEntity.setTeam(teamEntity);
        PlayerEntity saved = playerService.save(playerEntity);
        return playerMapper.toDto(saved);
    }

    @Transactional
    public PlayerResponseDto assign(Long playerId, PlayerAssignRequestDto dto) {
        PlayerEntity saved = playerService.assign(playerId, new TeamId(dto.teamId()));
        return playerMapper.toDto(saved);
    }
}
