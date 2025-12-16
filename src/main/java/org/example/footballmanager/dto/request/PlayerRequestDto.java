package org.example.footballmanager.dto.request;

public record PlayerRequestDto(
        String firstName,
        String lastName,
        String middleName,
        Long teamId
) {}


