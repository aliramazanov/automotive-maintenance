package com.automotive.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ReqCarDto(
        @NotNull(message = "required")
        Integer modelId,

        @NotBlank(message = "required")
        @Size(max = 20, message = "vin can be max 20")
        String vin,

        @NotBlank(message = "required")
        @Size(max = 10, message = "registration number can be max 10")
        String registrationNumber,

        @NotNull(message = "required")
        @PositiveOrZero(message = "mileageKm must be >= 0")
        Integer mileageKm,

        Integer productionYear,

        @Valid
        ReqDetailDto detail,

        List<Integer> featureIds
) {
}
