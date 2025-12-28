package org.example.footballmanager.service;

import lombok.RequiredArgsConstructor;
import org.example.footballmanager.domain.Player;
import org.example.footballmanager.domain.TeamId;
import org.example.footballmanager.entity.PlayerEntity;
import org.example.footballmanager.entity.TeamEntity;
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
    private final PlayerMapper playerMapper;
    private final TeamService teamService;

    public PlayerEntity save(PlayerEntity playerEntity) {
        return playerRepository.save(playerEntity);
    }

    public PlayerEntity create(Player player) {
        PlayerEntity playerEntity = playerMapper.toEntity(player);

        if (player.getTeamId() != null) {
            playerEntity.setTeam(teamService.findById(player.getTeamId().id()));
        }

        return save(playerEntity);
    }

    public PlayerEntity findById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));
    }

    public Page<PlayerEntity> findAll(Pageable pageable) {
        return playerRepository
                .findAll(pageable);
    }

    public Page<PlayerEntity> findAllByTeamId(Long teamId, Pageable pageable) {
        return playerRepository
                .findAllByTeam_Id(teamId, pageable);
    }

    public void deleteById(Long id) {
        PlayerEntity playerEntity = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));
        playerRepository.delete(playerEntity);
    }

    public PlayerEntity assign(Long playerId, TeamId teamId) {
        PlayerEntity playerEntity = findById(playerId);

        TeamEntity teamEntity = null;
        if (teamId.id() != null) {
            teamEntity = teamService.findById(teamId.id());
        }

        playerEntity.setTeam(teamEntity);
        return save(playerEntity);
    }
}
