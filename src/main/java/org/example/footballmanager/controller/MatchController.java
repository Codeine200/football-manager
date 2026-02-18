package org.example.footballmanager.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.footballmanager.dto.request.MatchFinishRequestDto;
import org.example.footballmanager.dto.request.MatchRequestDto;
import org.example.footballmanager.dto.response.MatchResponseDto;
import org.example.footballmanager.dto.response.PageResponse;
import org.example.footballmanager.facade.MatchFacade;
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
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchFacade matchFacade;

    @GetMapping
    public PageResponse<MatchResponseDto> getAll(Pageable pageable) {
        return matchFacade.findAll(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MatchResponseDto createMatch(@RequestBody MatchRequestDto request) {
        return matchFacade.createMatch(request);
    }

    @PostMapping("/{id}/finish")
    @ResponseStatus(HttpStatus.OK)
    public MatchResponseDto finishMatch(@PathVariable Long id, @RequestBody MatchFinishRequestDto request) {
        return matchFacade.finishMatch(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        matchFacade.deleteById(id);
    }

    @PutMapping("/{id}")
    public MatchResponseDto update(@PathVariable Long id,
                                  @RequestBody @Valid MatchRequestDto dto) {
        return matchFacade.update(id, dto);
    }
}
