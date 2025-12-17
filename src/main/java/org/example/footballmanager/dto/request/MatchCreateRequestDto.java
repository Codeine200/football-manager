package org.example.footballmanager.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MatchCreateRequestDto {
    private Long season;
    private LocalDate matchDate;
    private TeamDto team1;
    private TeamDto team2;

    @Data
    public static class TeamDto {
        private Long id;
        private boolean isGuest;
    }
}
