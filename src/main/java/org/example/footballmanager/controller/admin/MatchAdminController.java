package org.example.footballmanager.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.footballmanager.dto.request.MatchFinishRequestDto;
import org.example.footballmanager.dto.request.MatchRequestDto;
import org.example.footballmanager.dto.request.MatchUpdateRequestDto;
import org.example.footballmanager.dto.response.MatchResponseDto;
import org.example.footballmanager.facade.MatchFacade;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/matches")
@RequiredArgsConstructor
public class MatchAdminController {

    private final MatchFacade matchFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public MatchResponseDto createMatch(@RequestBody MatchRequestDto request) {
        return matchFacade.createMatch(request);
    }

    @PostMapping("/{id}/finish")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public MatchResponseDto finishMatch(@PathVariable Long id, @RequestBody MatchFinishRequestDto request) {
        return matchFacade.finishMatch(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        matchFacade.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public MatchResponseDto update(@PathVariable Long id,
                                   @RequestBody @Valid MatchUpdateRequestDto dto) {
        return matchFacade.update(id, dto);
    }
}
