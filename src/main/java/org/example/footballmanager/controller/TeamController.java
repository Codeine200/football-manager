package org.example.footballmanager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.footballmanager.dto.request.TeamRequestDto;
import org.example.footballmanager.dto.response.PageResponse;
import org.example.footballmanager.dto.response.TeamResponseDto;
import org.example.footballmanager.facade.TeamFacade;
import org.example.footballmanager.service.TeamService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final TeamFacade teamFacade;

    @GetMapping
    public PageResponse<TeamResponseDto> getAll(Pageable pageable) {
        return teamFacade.findAll(pageable);
    }

    @GetMapping("/{id}")
    public TeamResponseDto getById(@PathVariable Long id) {
        return teamFacade.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeamResponseDto create(@RequestBody @Valid TeamRequestDto dto) {
        return teamFacade.save(dto);
    }

    @PutMapping("/{id}")
    public TeamResponseDto update(@PathVariable Long id,
                                  @RequestBody @Valid TeamRequestDto dto) {
        return teamFacade.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        teamFacade.deleteById(id);
    }
}
