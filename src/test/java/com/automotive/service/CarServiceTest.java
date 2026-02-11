package com.automotive.service;

import com.automotive.dto.ReqCarDto;
import com.automotive.dto.ReqDetailDto;
import com.automotive.dto.RespCarDto;
import com.automotive.exception.CarException;
import com.automotive.model.Brand;
import com.automotive.model.Car;
import com.automotive.model.Detail;
import com.automotive.model.Model;
import com.automotive.repository.CarRepository;
import com.automotive.repository.FeatureRepository;
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
class CarServiceTest {
    
    @Mock
    private CarRepository carRepository;
    
    @Mock
    private ModelRepository modelRepository;
    
    @Mock
    private FeatureRepository featureRepository;
    
    @InjectMocks
    private CarServiceImpl carService;
    
    private Model model;
    private Car car;
    
    @BeforeEach
    void setUp () {
        Brand brand = Brand.builder().id(1).name("Toyota").country("Japan").build();
        model = Model.builder().id(1).name("Camry").category("Sedan").brand(brand).build();
        car = Car.builder()
                .id(1)
                .vin("ABC123")
                .registrationNumber("10AA001")
                .mileageKm(50000)
                .productionYear(2022)
                .model(model)
                .build();
    }
    
    @Test
    void getCars_shouldReturnAllCars () {
        when(carRepository.findAll()).thenReturn(List.of(car));
        
        List<RespCarDto> result = carService.getCars();
        
        assertEquals(1, result.size());
        assertEquals("ABC123", result.getFirst().vin());
        assertEquals("Camry", result.getFirst().modelName());
    }
    
    @Test
    void getCarById_shouldReturnCar_whenExists () {
        when(carRepository.findById(1)).thenReturn(Optional.of(car));
        
        RespCarDto result = carService.getCarById(1);
        
        assertNotNull(result);
        assertEquals("ABC123", result.vin());
        assertEquals("Toyota", result.brandName());
    }
    
    @Test
    void getCarById_shouldThrowException_whenNotFound () {
        when(carRepository.findById(99)).thenReturn(Optional.empty());
        
        assertThrows(CarException.class, () -> carService.getCarById(99));
    }
    
    @Test
    void addCar_shouldSaveCar () {
        when(modelRepository.findById(1)).thenReturn(Optional.of(model));
        ReqCarDto request = new ReqCarDto(1, "XYZ789", "20BB002", 0, 2023, null, null);
        
        carService.addCar(request);
        
        verify(carRepository, times(1)).save(any(Car.class));
    }
    
    @Test
    void addCar_shouldThrowException_whenModelNotFound () {
        when(modelRepository.findById(99)).thenReturn(Optional.empty());
        ReqCarDto request = new ReqCarDto(99, "XYZ789", "20BB002", 0, 2023, null, null);
        
        assertThrows(CarException.class, () -> carService.addCar(request));
    }
    
    @Test
    void addCar_shouldSaveCarWithDetail () {
        when(modelRepository.findById(1)).thenReturn(Optional.of(model));
        ReqDetailDto detail = new ReqDetailDto("ENG123", "REG123", "Petrol", "2.0L", "Red", null);
        ReqCarDto request = new ReqCarDto(1, "XYZ789", "20BB002", 10000, 2023, detail, null);
        
        carService.addCar(request);
        
        verify(carRepository, times(1)).save(any(Car.class));
    }
    
    @Test
    void deleteCar_shouldDeleteCar_whenExists () {
        when(carRepository.findById(1)).thenReturn(Optional.of(car));
        
        carService.deleteCarById(1);
        
        verify(carRepository, times(1)).delete(car);
    }
    
    @Test
    void deleteCar_shouldThrowException_whenNotFound () {
        when(carRepository.findById(99)).thenReturn(Optional.empty());
        
        assertThrows(CarException.class, () -> carService.deleteCarById(99));
    }
    
    @Test
    void updateCar_shouldUpdateFields () {
        when(carRepository.findById(1)).thenReturn(Optional.of(car));
        ReqCarDto request = new ReqCarDto(1, "NEWVIN", "30CC003", 60000, 2023, null, null);
        
        carService.updateCar(1, request);
        
        assertEquals("NEWVIN", car.getVin());
        assertEquals("30CC003", car.getRegistrationNumber());
        assertEquals(60000, car.getMileageKm());
        verify(carRepository, times(1)).save(car);
    }
    
    @Test
    void updateCar_shouldCreateDetailIfNotExists () {
        when(carRepository.findById(1)).thenReturn(Optional.of(car));
        ReqDetailDto detail = new ReqDetailDto("ENG999", "REG999", "Diesel", "3.0L", "Blue", null);
        ReqCarDto request = new ReqCarDto(1, "ABC123", "10AA001", 50000, 2022, detail, null);
        
        carService.updateCar(1, request);
        
        assertNotNull(car.getDetail());
        assertEquals("ENG999", car.getDetail().getEngineNumber());
    }
    
    @Test
    void updateCar_shouldUpdateExistingDetail () {
        Detail existingDetail = Detail
                .builder()
                .id(1).car(car).engineNumber("OLD").registrationCode("OLD")
                .fuelType("Petrol").engineCapacity("1.6L").build();
        car.setDetail(existingDetail);
        
        when(carRepository.findById(1)).thenReturn(Optional.of(car));
        ReqDetailDto detail = new ReqDetailDto("NEW", "NEW", "Diesel", "2.0L", "Black", "INS001");
        ReqCarDto request = new ReqCarDto(1, "ABC123", "10AA001", 50000, 2022, detail, null);
        
        carService.updateCar(1, request);
        
        assertEquals("NEW", existingDetail.getEngineNumber());
        assertEquals("Diesel", existingDetail.getFuelType());
    }
}
