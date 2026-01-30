package com.automotive.dto;

import jakarta.validation.constraints.NotBlank;

public record ModelDto(
        Integer id,
        @NotBlank
        String name,
        String category,
        Integer yearFrom,
        Integer yearTo
) {
}
