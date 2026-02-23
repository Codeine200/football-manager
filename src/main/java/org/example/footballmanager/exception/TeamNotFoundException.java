package org.example.footballmanager.exception;

public class TeamNotFoundException extends RuntimeException {
    public TeamNotFoundException(Long id) {
        super("Team with id " + id + " not found");
    }
}

