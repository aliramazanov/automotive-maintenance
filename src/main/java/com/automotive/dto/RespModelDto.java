package com.automotive.dto;

public record RespModelDto(
        Integer id,
        String name,
        String category,
        Integer yearFrom,
        Integer yearTo,
        Integer brandId,
        String brandName
) {
}
