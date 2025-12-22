package org.example.footballmanager.dto.request;

import lombok.Data;

@Data
public class MatchFinishRequestDto {
    private TeamDto team1;
    private TeamDto team2;

    @Data
    public static class TeamDto {
        private Long teamId;
        private int goals;
    }
}
