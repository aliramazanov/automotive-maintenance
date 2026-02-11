package com.automotive.dto;

import com.automotive.annotation.LogIgnore;

public record DetailDto(
        Integer id,
        String engineNumber,
        String registrationCode,
        String color,
        @LogIgnore
        String insuranceNumber,
        String fuelType,
        String engineCapacity
) {
}
