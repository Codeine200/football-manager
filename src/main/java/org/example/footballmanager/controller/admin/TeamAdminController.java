package org.example.footballmanager.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.footballmanager.dto.request.TeamRequestDto;
import org.example.footballmanager.dto.response.TeamResponseDto;
import org.example.footballmanager.facade.TeamFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/teams")
@RequiredArgsConstructor
public class TeamAdminController {

    private final TeamFacade teamFacade;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public TeamResponseDto create(
            @RequestPart("team") @Valid TeamRequestDto dto,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        return teamFacade.save(dto, file);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public TeamResponseDto update(@PathVariable Long id,
                                  @RequestPart("team") @Valid TeamRequestDto dto,
                                  @RequestPart(value = "file", required = false) MultipartFile file) {
        return teamFacade.update(id, dto, file);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        teamFacade.deleteById(id);
    }

    @DeleteMapping("/{id}/photo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteLogo(@PathVariable Long id) {
        teamFacade.deleteLogoByTeamId(id);
    }
}
