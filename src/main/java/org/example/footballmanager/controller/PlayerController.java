package org.example.footballmanager.controller;

import lombok.RequiredArgsConstructor;
import org.example.footballmanager.dto.request.PlayerRequestDto;
import org.example.footballmanager.dto.response.PageResponse;
import org.example.footballmanager.dto.response.PlayerResponseDto;
import org.example.footballmanager.dto.response.TeamResponseDto;
import org.example.footballmanager.facade.PlayerFacade;
import org.example.footballmanager.service.PlayerService;
import org.springframework.data.domain.Page;
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
@RequestMapping("/api/v1/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerFacade playerFacade;

    @GetMapping
    public PageResponse<PlayerResponseDto> getAll(Pageable pageable) {
        return playerFacade.findAll(pageable);
    }


    @GetMapping("/{id}")
    public PlayerResponseDto getById(@PathVariable Long id) {
        return playerFacade.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerResponseDto create(@RequestBody PlayerRequestDto dto) {
        return playerFacade.save(dto);
    }

    @PutMapping("/{id}")
    public PlayerResponseDto update(
            @PathVariable Long id,
            @RequestBody PlayerRequestDto dto
    ) {
        return playerFacade.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        playerService.deleteById(id);
    }
}

