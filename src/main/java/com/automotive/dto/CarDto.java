package com.automotive.dto;

import java.util.List;

public record CarDto(
        Integer id,
        String vin,
        String registrationNumber,
        Integer mileageKm,
        Integer productionYear,
        Integer modelId,
        CarDetailsDto carDetails,
        List<Integer> featureIds
) {
}
