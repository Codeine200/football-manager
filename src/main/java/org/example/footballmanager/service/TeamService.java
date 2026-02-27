package org.example.footballmanager.service;

import lombok.RequiredArgsConstructor;
import org.example.footballmanager.entity.TeamEntity;
import org.example.footballmanager.exception.TeamNotFoundException;
import org.example.footballmanager.repository.MatchRepository;
import org.example.footballmanager.repository.MatchStatsRepository;
import org.example.footballmanager.repository.PlayerRepository;
import org.example.footballmanager.repository.TeamRepository;
import org.example.footballmanager.store.TeamFileStorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final MatchStatsRepository matchStatsRepository;
    private final TeamFileStorageService fileStorageService;

    public TeamEntity save(TeamEntity teamEntity, MultipartFile file) {
        TeamEntity savedTeamEntity = teamRepository.save(teamEntity);
        if (file != null && !file.isEmpty()) {
            String savedFileName = fileStorageService.saveFile(file, savedTeamEntity.getId().toString());
            savedTeamEntity.setLogo(savedFileName);
            teamRepository.save(savedTeamEntity);
        }
        return teamEntity;
    }

    public TeamEntity findById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(id));
    }

    public TeamEntity getReferenceById(Long id) {
        return teamRepository.getReferenceById(id);
    }

    public Page<TeamEntity> findAll(Pageable pageable) {
        return teamRepository
                .findAll(pageable);
    }

    public void deleteById(Long id) {
        TeamEntity teamEntity = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(id));
        playerRepository.detachPlayersFromTeam(id);
        List<Long> matchesIds = matchStatsRepository.findMatchIdsByTeamId(id);
        matchStatsRepository.deleteStatsByMatchIds(matchesIds);
        matchRepository.deleteMatchesByIds(matchesIds);
        teamRepository.delete(teamEntity);
    }
}
