package org.example.footballmanager.service;

import lombok.RequiredArgsConstructor;
import org.example.footballmanager.dto.request.PlayerRequestDto;
import org.example.footballmanager.dto.response.PlayerResponseDto;
import org.example.footballmanager.entity.Player;
import org.example.footballmanager.exception.PlayerNotFoundException;
import org.example.footballmanager.mapper.PlayerMapper;
import org.example.footballmanager.repository.PlayerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final TeamService teamService;
    private final PlayerMapper playerMapper;

    public PlayerResponseDto save(PlayerRequestDto dto) {
        Player player = playerMapper.toEntity(dto);

        if (dto.teamId() != null) {
            player.setTeam(teamService.findById(dto.teamId()));
        }

        Player saved = playerRepository.save(player);
        return playerMapper.toDto(saved);
    }

    public Player findById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));
    }

    public Page<Player> findAll(Pageable pageable) {
        return playerRepository.findAll(pageable);
    }

    public void deleteById(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));
        playerRepository.delete(player);
    }

}
