package org.example.footballmanager.service;

import lombok.RequiredArgsConstructor;
import org.example.footballmanager.entity.Player;
import org.example.footballmanager.exception.PlayerNotFoundException;
import org.example.footballmanager.repository.PlayerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    public Player save(Player player) {
        return playerRepository.save(player);
    }

    public Player findById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));
    }

    public Page<Player> findAll(Pageable pageable) {
        return playerRepository
                .findAll(pageable);
    }

    public Page<Player> findAllByTeamId(Long teamId, Pageable pageable) {
        return playerRepository
                .findAllByTeam_Id(teamId, pageable);
    }

    public void deleteById(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));
        playerRepository.delete(player);
    }
}
