package org.example.footballmanager.domain;


import lombok.Getter;

@Getter
public class MatchFinish {

    private final MatchTeamResult team1;
    private final MatchTeamResult team2;

    public MatchFinish(MatchTeamResult team1, MatchTeamResult team2) {
        if (team1 == null || team2 == null) {
            throw new IllegalArgumentException("Both team results must be provided");
        }
        if (team1.getTeamId().equals(team2.getTeamId())) {
            throw new IllegalArgumentException("Teams must be different");
        }

        this.team1 = team1;
        this.team2 = team2;
    }
}
