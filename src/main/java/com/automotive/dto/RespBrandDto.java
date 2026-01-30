package com.automotive.dto;

import java.util.List;

public record RespBrandDto(
        Integer id,
        String name,
        String country,
        List<ModelDto> models
) {
}
