package com.automotive.dto;

public record CarDetailsDto(
        Integer id,
        String engineNumber,
        String registrationCode,
        String color,
        String insuranceNumber,
        String fuelType,
        String engineCapacity
) {
}

