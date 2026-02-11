package com.automotive.dto;

import com.automotive.annotation.LogIgnore;

import java.util.List;

public record RespCarDto(
        Integer id,
        @LogIgnore
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
