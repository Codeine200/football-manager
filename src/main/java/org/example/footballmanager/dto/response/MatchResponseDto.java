package org.example.footballmanager.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MatchResponseDto {

    private Long id;
    private LocalDateTime matchDateTime;
    private String status;

    private TeamStatsDto team1;
    private TeamStatsDto team2;

    @Data
    public static class TeamStatsDto {
        private Long id;
        private String name;
        private boolean isGuest;
        private Integer goals;
        private Integer score;
        private Boolean isWinner;
        private String imageUrl;
    }
}

