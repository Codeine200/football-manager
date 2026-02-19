package org.example.footballmanager.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MatchFullInfo {

    private final Integer season;
    private final LocalDate matchDate;
    private final TeamFullInfo team1;
    private final TeamFullInfo team2;
    private final boolean isFinished;

    @Builder
    public MatchFullInfo(
            Integer season,
            LocalDate matchDate,
            TeamFullInfo team1,
            TeamFullInfo team2,
            boolean isFinished
    ) {
        if (season == null) {
            throw new IllegalArgumentException("SeasonId must not be null");
        }
        if (matchDate == null) {
            throw new IllegalArgumentException("Match date must not be null");
        }
        if (team1 == null || team2 == null) {
            throw new IllegalArgumentException("Both teams must be provided");
        }
        if (team1.getTeamId().equals(team2.getTeamId())) {
            throw new IllegalArgumentException("Teams must be different");
        }
        if (team1.isGuest() == team2.isGuest()) {
            throw new IllegalArgumentException("One team must be guest and the other must be home");
        }

        this.season = season;
        this.matchDate = matchDate;
        this.team1 = team1;
        this.team2 = team2;
        this.isFinished = isFinished;
    }
}
