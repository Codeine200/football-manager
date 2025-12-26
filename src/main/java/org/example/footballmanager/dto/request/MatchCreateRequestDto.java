package org.example.footballmanager.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MatchCreateRequestDto {
    @NotNull
    private Long season;
    @NotNull
    private LocalDate matchDate;
    @NotNull
    @Valid
    private TeamDto team1;
    @NotNull
    @Valid
    private TeamDto team2;

    @Data
    public static class TeamDto {
        @NotNull
        private Long id;
        @JsonProperty("isGuest")
        private boolean isGuest;
    }
}
