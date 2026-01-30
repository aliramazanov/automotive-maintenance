package com.automotive.dto;

public record DetailDto(
        Integer id,
        String engineNumber,
        String registrationCode,
        String color,
        String insuranceNumber,
        String fuelType,
        String engineCapacity
) {
}
