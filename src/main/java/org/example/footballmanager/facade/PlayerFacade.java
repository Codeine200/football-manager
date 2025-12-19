package org.example.footballmanager.facade;

import lombok.RequiredArgsConstructor;
import org.example.footballmanager.dto.request.PlayerRequestDto;
import org.example.footballmanager.dto.response.PageResponse;
import org.example.footballmanager.dto.response.PlayerResponseDto;
import org.example.footballmanager.dto.response.TeamResponseDto;
import org.example.footballmanager.entity.Player;
import org.example.footballmanager.entity.Team;
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

    public PlayerResponseDto findById(Long id) {
        Player player = playerService.findById(id);
        return playerMapper.toDto(player);
    }

    public PageResponse<PlayerResponseDto> findAll(Pageable pageable) {
        Page<PlayerResponseDto> page = playerService
                .findAll(pageable)
                .map(playerMapper::toDto);

        return pageMapper.toDto(page);
    }

    public PlayerResponseDto save(PlayerRequestDto dto) {
        Player player = playerMapper.toEntity(dto);

        if (dto.teamId() != null) {
            player.setTeam(teamService.findById(dto.teamId()));
        }

        Player saved = playerService.save(player);
        return playerMapper.toDto(saved);
    }

    public PlayerResponseDto update(Long playerId, PlayerRequestDto dto) {
        Player player = playerService.findById(playerId);

        Team team = null;
        if (dto.teamId() != null) {
            team = teamService.findById(dto.teamId());
        }

        playerMapper.updateFromDto(dto, player);
        player.setTeam(team);
        Player saved = playerService.save(player);
        return playerMapper.toDto(saved);
    }
}
