package org.example.footballmanager.domain;


import lombok.Getter;

@Getter
public class MatchTeamResult {

    private final TeamId teamId;
    private final int goals;

    public MatchTeamResult(TeamId teamId, int goals) {
        if (teamId == null) {
            throw new IllegalArgumentException("TeamId must not be null");
        }
        if (goals < 0) {
            throw new IllegalArgumentException("Goals must be >= 0");
        }

        this.teamId = teamId;
        this.goals = goals;
    }
}