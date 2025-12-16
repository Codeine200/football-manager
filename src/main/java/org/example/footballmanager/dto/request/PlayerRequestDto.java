package org.example.footballmanager.dto.request;

public record PlayerRequestDto(
        Long id,
        String firstName,
        String lastName,
        String middleName,
        Long teamId
) {}


