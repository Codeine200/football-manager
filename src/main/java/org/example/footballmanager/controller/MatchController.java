package org.example.footballmanager.controller;


import lombok.RequiredArgsConstructor;
import org.example.footballmanager.dto.request.MatchCreateRequestDto;
import org.example.footballmanager.dto.request.MatchFinishRequestDto;
import org.example.footballmanager.dto.response.MatchResponseDto;
import org.example.footballmanager.facade.MatchFacade;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchFacade matchFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MatchResponseDto createMatch(@RequestBody MatchCreateRequestDto request) {
        return matchFacade.createMatch(request);
    }

    @PostMapping("/{id}/finish")
    @ResponseStatus(HttpStatus.OK)
    public MatchResponseDto finishMatch(@PathVariable Long id, @RequestBody MatchFinishRequestDto request) {
        return matchFacade.finishMatch(id, request);
    }
}
