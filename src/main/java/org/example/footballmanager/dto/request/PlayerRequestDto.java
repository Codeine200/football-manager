package org.example.footballmanager.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PlayerRequestDto(
        @NotBlank(message = "Full name must not be blank")
        String fullName,
        Long teamId
) {}


