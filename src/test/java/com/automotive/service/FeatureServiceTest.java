package com.automotive.service;

import com.automotive.dto.FeatureDto;
import com.automotive.dto.ReqFeatureDto;
import com.automotive.exception.CarException;
import com.automotive.model.Feature;
import com.automotive.repository.FeatureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeatureServiceTest {

    @Mock
    private FeatureRepository featureRepository;

    @InjectMocks
    private FeatureServiceImpl featureService;

    private Feature feature;

    @BeforeEach
    void setUp() {
        feature = Feature.builder()
                .id(1)
                .featureId(101)
                .name("ABS")
                .description("Anti-lock Braking System")
                .category("Safety")
                .build();
    }

    @Test
    void getFeatures_shouldReturnAllFeatures() {
        when(featureRepository.findAll()).thenReturn(List.of(feature));

        List<FeatureDto> result = featureService.getFeatures();

        assertEquals(1, result.size());
        assertEquals("ABS", result.get(0).name());
        verify(featureRepository, times(1)).findAll();
    }

    @Test
    void getFeatureById_shouldReturnFeature_whenExists() {
        when(featureRepository.findById(1)).thenReturn(Optional.of(feature));

        FeatureDto result = featureService.getFeatureById(1);

        assertNotNull(result);
        assertEquals("ABS", result.name());
        assertEquals("Safety", result.category());
    }

    @Test
    void getFeatureById_shouldThrowException_whenNotFound() {
        when(featureRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(CarException.class, () -> featureService.getFeatureById(99));
    }

    @Test
    void addFeature_shouldSaveFeature() {
        ReqFeatureDto request = new ReqFeatureDto(102, "Sunroof", "Glass roof panel", "Comfort");

        featureService.addFeature(request);

        verify(featureRepository, times(1)).save(any(Feature.class));
    }

    @Test
    void deleteFeatureById_shouldDeleteFeature_whenExists() {
        when(featureRepository.findById(1)).thenReturn(Optional.of(feature));

        featureService.deleteFeatureById(1);

        verify(featureRepository, times(1)).delete(feature);
    }

    @Test
    void deleteFeatureById_shouldThrowException_whenNotFound() {
        when(featureRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(CarException.class, () -> featureService.deleteFeatureById(99));
    }

    @Test
    void updateFeature_shouldUpdateFeature_whenExists() {
        when(featureRepository.findById(1)).thenReturn(Optional.of(feature));
        ReqFeatureDto request = new ReqFeatureDto(101, "ABS Updated", "Updated description", "Safety");

        featureService.updateFeature(1, request);

        assertEquals("ABS Updated", feature.getName());
        verify(featureRepository, times(1)).save(feature);
    }
}
