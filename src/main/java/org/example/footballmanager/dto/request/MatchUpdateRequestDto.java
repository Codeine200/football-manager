package org.example.footballmanager.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MatchUpdateRequestDto (
    @NotNull
    Long season,
    @NotNull
    LocalDate matchDate,
    @NotNull
    @Valid
    TeamDto team1,
    @NotNull
    @Valid
    TeamDto team2,
    boolean isFinished
) {
    public record TeamDto (
        @NotNull
        Long id,
        @JsonProperty("isGuest")
        boolean isGuest,
        int goals
    ) {}
}
