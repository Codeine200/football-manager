package org.example.footballmanager.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Player {

    private final String firstName;
    private final String lastName;
    private final String middleName;
    private final TeamId teamId;

    @Builder
    public Player(String firstName, String lastName, String middleName, TeamId teamId) {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name must not be blank");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name must not be blank");
        }
        if (teamId == null) {
            throw new IllegalArgumentException("Team must not be null");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.teamId = teamId;
    }
}

