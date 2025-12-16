package org.example.footballmanager.service;

import lombok.RequiredArgsConstructor;
import org.example.footballmanager.entity.Team;
import org.example.footballmanager.repository.TeamRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    public Team save(Team team) {
        return teamRepository.save(team);
    }

    public Optional<Team> findById(Long id) {
        return teamRepository.findById(id);
    }

    public Page<Team> findAll(Pageable pageable) {
        return teamRepository.findAll(pageable);
    }

    public void deleteById(Long id) {
        teamRepository.deleteById(id);
    }
}
