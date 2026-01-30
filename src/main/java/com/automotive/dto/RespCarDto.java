package com.automotive.dto;

import java.util.List;

public record RespCarDto(
        Integer id,
        String vin,
        String registrationNumber,
        Integer mileageKm,
        Integer productionYear,
        Integer modelId,
        String modelName,
        Integer brandId,
        String brandName,
        DetailDto detail,
        List<FeatureDto> features
) {
}
