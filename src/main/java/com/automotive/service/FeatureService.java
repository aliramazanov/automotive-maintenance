package com.automotive.service;

import com.automotive.dto.FeatureDto;
import com.automotive.dto.ReqFeatureDto;

import java.util.List;

public interface FeatureService {

    List<FeatureDto> getFeatures();

    FeatureDto getFeatureById(int id);

    void addFeature(ReqFeatureDto featureDto);

    void deleteFeatureById(int id);

    void updateFeature(int id, ReqFeatureDto featureDto);
}
