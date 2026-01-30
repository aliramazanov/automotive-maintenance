package com.automotive.dto;

import jakarta.validation.constraints.Size;

public record ReqFeatureDto(
        Integer featureId,

        @Size(max = 255, message = "max 255")
        String name,

        @Size(max = 255, message = "max 255")
        String description,

        @Size(max = 255, message = "max 255")
        String category
) {
}
