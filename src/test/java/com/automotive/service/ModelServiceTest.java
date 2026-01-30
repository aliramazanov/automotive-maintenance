package com.automotive.service;

import com.automotive.dto.ReqModelDto;
import com.automotive.dto.RespModelDto;
import com.automotive.exception.CarException;
import com.automotive.model.Brand;
import com.automotive.model.Model;
import com.automotive.repository.BrandRepository;
import com.automotive.repository.ModelRepository;
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
class ModelServiceTest {

    @Mock
    private ModelRepository modelRepository;

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private ModelServiceImpl modelService;

    private Brand brand;
    private Model model;

    @BeforeEach
    void setUp() {
        brand = Brand.builder()
                .id(1)
                .name("Toyota")
                .country("Japan")
                .build();

        model = Model.builder()
                .id(1)
                .name("Camry")
                .category("Sedan")
                .yearFrom(2020)
                .yearTo(2024)
                .brand(brand)
                .build();
    }

    @Test
    void getModels_shouldReturnAllModels() {
        when(modelRepository.findAll()).thenReturn(List.of(model));

        List<RespModelDto> result = modelService.getModels();

        assertEquals(1, result.size());
        assertEquals("Camry", result.get(0).name());
        assertEquals("Toyota", result.get(0).brandName());
    }

    @Test
    void getModelById_shouldReturnModel_whenExists() {
        when(modelRepository.findById(1)).thenReturn(Optional.of(model));

        RespModelDto result = modelService.getModelById(1);

        assertNotNull(result);
        assertEquals("Camry", result.name());
        assertEquals("Sedan", result.category());
    }

    @Test
    void getModelById_shouldThrowException_whenNotFound() {
        when(modelRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(CarException.class, () -> modelService.getModelById(99));
    }

    @Test
    void addModel_shouldSaveModel_whenBrandExists() {
        when(brandRepository.findById(1)).thenReturn(Optional.of(brand));
        ReqModelDto request = new ReqModelDto(1, "Corolla", "Sedan", 2020, 2024);

        modelService.addModel(request);

        verify(modelRepository, times(1)).save(any(Model.class));
    }

    @Test
    void addModel_shouldThrowException_whenBrandNotFound() {
        when(brandRepository.findById(99)).thenReturn(Optional.empty());
        ReqModelDto request = new ReqModelDto(99, "Corolla", "Sedan", 2020, 2024);

        assertThrows(CarException.class, () -> modelService.addModel(request));
    }

    @Test
    void deleteModelById_shouldDeleteModel_whenExists() {
        when(modelRepository.findById(1)).thenReturn(Optional.of(model));

        modelService.deleteModelById(1);

        verify(modelRepository, times(1)).delete(model);
    }

    @Test
    void updateModel_shouldUpdateModel_whenExists() {
        when(modelRepository.findById(1)).thenReturn(Optional.of(model));
        ReqModelDto request = new ReqModelDto(1, "Camry Updated", "Sedan", 2021, 2025);

        modelService.updateModel(1, request);

        assertEquals("Camry Updated", model.getName());
        verify(modelRepository, times(1)).save(model);
    }
}
