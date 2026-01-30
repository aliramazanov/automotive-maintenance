package com.automotive.exception.base;

public record BaseErrorResponseDTO(
        String code,
        String message,
        String path,
        String timestamp,
        Integer status,
        Object... data
) {
}
