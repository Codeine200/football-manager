package org.example.footballmanager.service;

import lombok.RequiredArgsConstructor;
import org.example.footballmanager.dto.request.TeamRequestDto;
import org.example.footballmanager.dto.response.TeamResponseDto;
import org.example.footballmanager.entity.Team;
import org.example.footballmanager.exception.TeamNotFoundException;
import org.example.footballmanager.mapper.TeamMapper;
import org.example.footballmanager.repository.TeamRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    public TeamResponseDto save(TeamRequestDto requestDto) {
        Team team = teamMapper.toEntity(requestDto);
        Team savedTeam = teamRepository.save(team);
        return teamMapper.toDto(savedTeam);
    }

    public TeamResponseDto update(Long id, TeamRequestDto dto) {
        Team team = findById(id);
        teamMapper.updateFromDto(dto, team);
        return teamMapper.toDto(teamRepository.save(team));
    }

    public Team findById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(id));
    }

    public Page<TeamResponseDto> findAll(Pageable pageable) {
        return teamRepository
                .findAll(pageable)
                .map(teamMapper::toDto);
    }

    public void deleteById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(id));
        teamRepository.delete(team);
    }
}
