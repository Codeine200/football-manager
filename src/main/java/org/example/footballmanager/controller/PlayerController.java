package org.example.footballmanager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.footballmanager.dto.request.PlayerAssignRequestDto;
import org.example.footballmanager.dto.request.PlayerRequestDto;
import org.example.footballmanager.dto.response.PageResponse;
import org.example.footballmanager.dto.response.PlayerResponseDto;
import org.example.footballmanager.facade.PlayerFacade;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerFacade playerFacade;

    @GetMapping
    public PageResponse<PlayerResponseDto> getAllByTeamId(
            @RequestParam(required = false) Long teamId,
            @RequestParam(required = false) String search,
            @PageableDefault(sort = {"fullName"}) Pageable pageable) {
        return playerFacade.findAllByTeamId(teamId, search, pageable);
    }

    @GetMapping("/{id}")
    public PlayerResponseDto getById(@PathVariable Long id) {
        return playerFacade.findById(id);
    }

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public PlayerResponseDto create(
            @RequestPart("player") @Valid PlayerRequestDto dto,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        return playerFacade.create(dto, file);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public PlayerResponseDto update(
            @PathVariable Long id,
            @RequestPart("player") @Valid PlayerRequestDto dto,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return playerFacade.update(id, dto, file);
    }

    @PutMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public PlayerResponseDto assign(
            @PathVariable Long id,
            @Valid @RequestBody PlayerAssignRequestDto dto
    ) {
        return playerFacade.assign(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        playerFacade.deleteById(id);
    }
}

