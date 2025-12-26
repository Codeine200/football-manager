package org.example.footballmanager.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PlayerRequestDto(
        @NotBlank(message = "First name must not be blank")
        String firstName,
        @NotBlank(message = "Last name must not be blank")
        String lastName,
        String middleName,
        Long teamId
) {}


