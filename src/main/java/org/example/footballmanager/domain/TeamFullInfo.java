package org.example.footballmanager.domain;

import lombok.Getter;

@Getter
public class TeamFullInfo {

    private final TeamId teamId;
    private final int goals;
    private final boolean isGuest;

    public TeamFullInfo(TeamId teamId, int goals, boolean isGuest) {
        if (teamId == null) {
            throw new IllegalArgumentException("TeamId must not be null");
        }
        if (goals < 0) {
            throw new IllegalArgumentException("Goals must be >= 0");
        }

        this.teamId = teamId;
        this.goals = goals;
        this.isGuest = isGuest;
    }
}
