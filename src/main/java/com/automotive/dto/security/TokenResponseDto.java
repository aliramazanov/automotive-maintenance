package com.automotive.dto.security;

public record TokenResponseDto(
        String accessToken,
        String refreshToken
) {
}
