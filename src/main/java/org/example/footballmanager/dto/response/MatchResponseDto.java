package org.example.footballmanager.dto.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MatchResponseDto {

    private Long id;
    private LocalDate matchDate;
    private String status;

    private TeamStatsDto team1;
    private TeamStatsDto team2;

    @Data
    public static class TeamStatsDto {
        private Long teamId;
        private String teamName;
        private boolean isGuest;
        private Integer goals;
        private Integer score;
        private Boolean isWinner;
    }
}

