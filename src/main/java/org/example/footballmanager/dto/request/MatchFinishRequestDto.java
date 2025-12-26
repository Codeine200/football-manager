package org.example.footballmanager.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MatchFinishRequestDto (
    @NotNull
    @Valid
    TeamDto team1,

    @NotNull
    @Valid
    TeamDto team2
) {
    public record TeamDto (
        @NotNull
        Long teamId,

        @Min(0)
        int goals
    ) {}
}