package org.example.footballmanager.service;

import lombok.RequiredArgsConstructor;
import org.example.footballmanager.entity.Player;
import org.example.footballmanager.repository.PlayerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    public Player save(Player player) {
        return playerRepository.save(player);
    }

    public Optional<Player> findById(Long id) {
        return playerRepository.findById(id);
    }

    public Page<Player> findAll(Pageable pageable) {
        return playerRepository.findAll(pageable);
    }

    public void deleteById(Long id) {
        playerRepository.deleteById(id);
    }
}
