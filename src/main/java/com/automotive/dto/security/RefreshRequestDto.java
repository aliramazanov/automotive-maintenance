package com.automotive.dto.security;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequestDto(
        @NotBlank(message = "refresh token is required")
        String refreshToken
) {
}
