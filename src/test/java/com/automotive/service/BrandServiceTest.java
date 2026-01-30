package com.automotive.service;

import com.automotive.dto.ReqBrandDto;
import com.automotive.dto.RespBrandDto;
import com.automotive.exception.CarException;
import com.automotive.model.Brand;
import com.automotive.repository.BrandRepository;
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
class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandServiceImpl brandService;

    private Brand brand;

    @BeforeEach
    void setUp() {
        brand = Brand.builder()
                .id(1)
                .name("Toyota")
                .country("Japan")
                .foundedYear(1937)
                .build();
    }

    @Test
    void getBrands_shouldReturnAllBrands() {
        when(brandRepository.findAll()).thenReturn(List.of(brand));

        List<RespBrandDto> result = brandService.getBrands();

        assertEquals(1, result.size());
        assertEquals("Toyota", result.getFirst().name());
        verify(brandRepository, times(1)).findAll();
    }

    @Test
    void getBrandById_shouldReturnBrand_whenExists() {
        when(brandRepository.findById(1)).thenReturn(Optional.of(brand));

        RespBrandDto result = brandService.getBrandById(1);

        assertNotNull(result);
        assertEquals("Toyota", result.name());
        assertEquals("Japan", result.country());
    }

    @Test
    void getBrandById_shouldThrowException_whenNotFound() {
        when(brandRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(CarException.class, () -> brandService.getBrandById(99));
    }

    @Test
    void addBrand_shouldSaveBrand() {
        ReqBrandDto request = new ReqBrandDto("BMW", "Germany", 1916, null);

        brandService.addBrand(request);

        verify(brandRepository, times(1)).save(any(Brand.class));
    }

    @Test
    void deleteBrandById_shouldDeleteBrand_whenExists() {
        when(brandRepository.findById(1)).thenReturn(Optional.of(brand));

        brandService.deleteBrandById(1);

        verify(brandRepository, times(1)).delete(brand);
    }

    @Test
    void deleteBrandById_shouldThrowException_whenNotFound() {
        when(brandRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(CarException.class, () -> brandService.deleteBrandById(99));
    }

    @Test
    void updateBrand_shouldUpdateBrand_whenExists() {
        when(brandRepository.findById(1)).thenReturn(Optional.of(brand));
        ReqBrandDto request = new ReqBrandDto("Toyota Updated", "Japan", 1937, null);

        brandService.updateBrand(1, request);

        assertEquals("Toyota Updated", brand.getName());
        verify(brandRepository, times(1)).save(brand);
    }
}
