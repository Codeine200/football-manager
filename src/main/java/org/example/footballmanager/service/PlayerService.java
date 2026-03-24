package org.example.footballmanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.footballmanager.domain.Player;
import org.example.footballmanager.domain.TeamId;
import org.example.footballmanager.entity.PlayerEntity;
import org.example.footballmanager.entity.TeamEntity;
import org.example.footballmanager.exception.PlayerNotFoundException;
import org.example.footballmanager.mapper.PlayerMapper;
import org.example.footballmanager.repository.PlayerRepository;
import org.example.footballmanager.store.PlayerFileStorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;
    private final TeamService teamService;
    private final PlayerFileStorageService fileStorageService;

    public PlayerEntity save(PlayerEntity playerEntity, MultipartFile file) {
        PlayerEntity savedPlayerEntity = playerRepository.save(playerEntity);
        if (file != null && !file.isEmpty()) {
            String savedFileName = fileStorageService.saveFile(file, savedPlayerEntity.getId().toString());
            savedPlayerEntity.setPhoto(savedFileName);
            playerRepository.save(savedPlayerEntity);
        }
        return savedPlayerEntity;
    }

    @Transactional
    public PlayerEntity create(Player player, MultipartFile file) {
        PlayerEntity playerEntity = playerMapper.toEntity(player);

        if (player.getTeamId() != null) {
            playerEntity.setTeam(teamService.findById(player.getTeamId().id()));
        }

        return save(playerEntity, file);
    }

    public PlayerEntity findById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));
    }

    public Page<PlayerEntity> findAll(Pageable pageable) {
        return playerRepository
                .findAll(pageable);
    }

    public Page<PlayerEntity> searchPlayers(Long teamId, String search, Pageable pageable) {
        return playerRepository
                .searchPlayers(teamId, search, pageable);
    }

    public Page<PlayerEntity> findAllByTeamId(Long teamId, Pageable pageable) {
        return playerRepository
                .findAllByTeam_Id(teamId, pageable);
    }

    @Transactional
    public PlayerEntity deleteById(Long id) {
        PlayerEntity playerEntity = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));
        playerRepository.delete(playerEntity);
        return playerEntity;
    }

    @Transactional
    public PlayerEntity assign(Long playerId, TeamId teamId) {
        PlayerEntity playerEntity = findById(playerId);

        TeamEntity teamEntity = null;
        if (teamId.id() != null) {
            teamEntity = teamService.findById(teamId.id());
        }

        playerEntity.setTeam(teamEntity);
        return save(playerEntity, null);
    }
}
