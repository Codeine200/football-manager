package org.example.footballmanager.facade;

import lombok.RequiredArgsConstructor;
import org.example.footballmanager.dto.request.TeamRequestDto;
import org.example.footballmanager.dto.response.PageResponse;
import org.example.footballmanager.dto.response.TeamResponseDto;
import org.example.footballmanager.entity.Team;
import org.example.footballmanager.mapper.PageMapper;
import org.example.footballmanager.mapper.TeamMapper;
import org.example.footballmanager.service.TeamService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamFacade {

    private final TeamService teamService;
    private final TeamMapper teamMapper;
    private final PageMapper pageMapper;

    public TeamResponseDto findById(Long id) {
        return teamMapper.toDto(teamService.findById(id));
    }

    public PageResponse<TeamResponseDto> findAll(Pageable pageable) {
        Page<TeamResponseDto> page = teamService
                .findAll(pageable)
                .map(teamMapper::toDto);

        return pageMapper.toDto(page);
    }

    public TeamResponseDto save(TeamRequestDto requestDto) {
        Team team = teamMapper.toEntity(requestDto);
        Team savedTeam = teamService.save(team);
        return teamMapper.toDto(savedTeam);
    }

    public TeamResponseDto update(Long id, TeamRequestDto dto) {
        Team team = teamService.findById(id);
        teamMapper.updateFromDto(dto, team);
        return teamMapper.toDto(teamService.save(team));
    }
}
