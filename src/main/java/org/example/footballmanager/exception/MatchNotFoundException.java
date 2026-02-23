package org.example.footballmanager.exception;

public class MatchNotFoundException  extends RuntimeException {
    public MatchNotFoundException(Long id) {
        super("Match with id " + id + " not found");
    }
}
