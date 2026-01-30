package com.automotive.service;

import com.automotive.dto.FeatureDto;
import com.automotive.dto.ReqFeatureDto;
import com.automotive.exception.CarErrorEnum;
import com.automotive.exception.CarException;
import com.automotive.model.Feature;
import com.automotive.repository.FeatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeatureServiceImpl implements FeatureService {

    private final FeatureRepository featureRepository;

    @Override
    @Transactional(readOnly = true)
    public List<FeatureDto> getFeatures() {
        return featureRepository.findAll()
                .stream()
                .map(FeatureServiceImpl::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FeatureDto getFeatureById(int id) {
        Feature feature = featureRepository.findById(id)
                .orElseThrow(() -> new CarException(CarErrorEnum.FEATURE_NOT_FOUND));
        return toDto(feature);
    }

    @Override
    @Transactional
    public void addFeature(ReqFeatureDto featureDto) {
        Feature feature = toEntity(featureDto);
        featureRepository.save(feature);
    }

    @Override
    @Transactional
    public void deleteFeatureById(int id) {
        Feature feature = featureRepository.findById(id)
                .orElseThrow(() -> new CarException(CarErrorEnum.FEATURE_NOT_FOUND));
        featureRepository.delete(feature);
    }

    @Override
    @Transactional
    public void updateFeature(int id, ReqFeatureDto featureDto) {
        Feature feature = featureRepository.findById(id)
                .orElseThrow(() -> new CarException(CarErrorEnum.FEATURE_NOT_FOUND));

        feature.setFeatureId(featureDto.featureId());
        feature.setName(featureDto.name());
        feature.setDescription(featureDto.description());
        feature.setCategory(featureDto.category());

        featureRepository.save(feature);
    }

    private static FeatureDto toDto(Feature feature) {
        return new FeatureDto(
                feature.getId(),
                feature.getFeatureId(),
                feature.getName(),
                feature.getDescription(),
                feature.getCategory()
        );
    }

    private static Feature toEntity(ReqFeatureDto dto) {
        return Feature.builder()
                .featureId(dto.featureId())
                .name(dto.name())
                .description(dto.description())
                .category(dto.category())
                .build();
    }
}
