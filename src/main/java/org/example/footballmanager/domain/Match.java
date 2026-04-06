package org.example.footballmanager.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Match {

    private final Long season;
    private final LocalDateTime matchDateTime;
    private final Team team1;
    private final Team team2;

    @Builder
    public Match(
            Long season,
            LocalDateTime matchDateTime,
            Team team1,
            Team team2
    ) {
        if (season == null) {
            throw new IllegalArgumentException("SeasonId must not be null");
        }
        if (matchDateTime == null) {
            throw new IllegalArgumentException("Match date time must not be null");
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
        this.matchDateTime = matchDateTime;
        this.team1 = team1;
        this.team2 = team2;
    }
}
