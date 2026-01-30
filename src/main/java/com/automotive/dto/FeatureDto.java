package com.automotive.dto;

public record FeatureDto(
        Integer id,
        Integer featureId,
        String name,
        String description,
        String category
) {
}
