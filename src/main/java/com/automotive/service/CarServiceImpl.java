package com.automotive.service;

import com.automotive.dto.DetailDto;
import com.automotive.dto.FeatureDto;
import com.automotive.dto.ReqDetailDto;
import com.automotive.dto.ReqCarDto;
import com.automotive.dto.RespCarDto;
import com.automotive.exception.CarErrorEnum;
import com.automotive.exception.CarException;
import com.automotive.model.Car;
import com.automotive.model.Detail;
import com.automotive.model.Model;
import com.automotive.model.Feature;
import com.automotive.repository.ModelRepository;
import com.automotive.repository.CarRepository;
import com.automotive.repository.FeatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final ModelRepository modelRepository;
    private final FeatureRepository featureRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RespCarDto> getCars() {
        return carRepository.findAll()
                .stream()
                .map(CarServiceImpl::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RespCarDto getCarById(int id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarException(CarErrorEnum.CAR_NOT_FOUND));
        return toDto(car);
    }

    @Override
    @Transactional
    public void addCar(ReqCarDto carDto) {
        Model model = modelRepository.findById(carDto.modelId())
                .orElseThrow(() -> new CarException(CarErrorEnum.MODEL_NOT_FOUND));

        Car car = toEntity(carDto, model);

        if (carDto.detail() != null) {
            Detail detail = toDetailEntity(carDto.detail(), car);
            car.setDetail(detail);
        }

        if (carDto.featureIds() != null && !carDto.featureIds().isEmpty()) {
            List<Feature> features = featureRepository.findAllById(carDto.featureIds());
            car.setFeatures(features);
        }

        carRepository.save(car);
    }

    @Override
    @Transactional
    public void deleteCarById(int id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarException(CarErrorEnum.CAR_NOT_FOUND));
        carRepository.delete(car);
    }

    @Override
    @Transactional
    public void updateCar(int id, ReqCarDto carDto) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarException(CarErrorEnum.CAR_NOT_FOUND));

        if (carDto.modelId() != null && !carDto.modelId().equals(car.getModel().getId())) {
            Model model = modelRepository.findById(carDto.modelId())
                    .orElseThrow(() -> new CarException(CarErrorEnum.MODEL_NOT_FOUND));
            car.setModel(model);
        }

        car.setVin(carDto.vin());
        car.setRegistrationNumber(carDto.registrationNumber());
        car.setMileageKm(carDto.mileageKm());
        car.setProductionYear(carDto.productionYear());

        if (carDto.detail() != null) {
            Detail detail = car.getDetail();

            if (detail == null) {
                detail = new Detail();
                detail.setCar(car);
                car.setDetail(detail);
            }

            ReqDetailDto d = carDto.detail();
            detail.setEngineNumber(d.engineNumber());
            detail.setRegistrationCode(d.registrationCode());
            detail.setFuelType(d.fuelType());
            detail.setEngineCapacity(d.engineCapacity());
            detail.setColor(d.color());
            detail.setInsuranceNumber(d.insuranceNumber());
        }

        if (carDto.featureIds() != null) {
            List<Feature> features = featureRepository.findAllById(carDto.featureIds());
            car.setFeatures(features);
        }

        carRepository.save(car);
    }

    private static RespCarDto toDto(Car car) {
        DetailDto detailDto = null;
        if (car.getDetail() != null) {
            Detail d = car.getDetail();
            detailDto = new DetailDto(
                    d.getId(),
                    d.getEngineNumber(),
                    d.getRegistrationCode(),
                    d.getColor(),
                    d.getInsuranceNumber(),
                    d.getFuelType(),
                    d.getEngineCapacity()
            );
        }

        List<FeatureDto> featureDtos = new ArrayList<>();
        if (car.getFeatures() != null) {
            featureDtos = car.getFeatures().stream()
                    .map(f -> new FeatureDto(
                            f.getId(),
                            f.getFeatureId(),
                            f.getName(),
                            f.getDescription(),
                            f.getCategory()
                    ))
                    .toList();
        }

        return new RespCarDto(
                car.getId(),
                car.getVin(),
                car.getRegistrationNumber(),
                car.getMileageKm(),
                car.getProductionYear(),
                car.getModel().getId(),
                car.getModel().getName(),
                car.getModel().getBrand().getId(),
                car.getModel().getBrand().getName(),
                detailDto,
                featureDtos
        );
    }

    private static Car toEntity(ReqCarDto dto, Model model) {
        return Car.builder()
                .model(model)
                .vin(dto.vin())
                .registrationNumber(dto.registrationNumber())
                .mileageKm(dto.mileageKm())
                .productionYear(dto.productionYear())
                .build();
    }

    private static Detail toDetailEntity(ReqDetailDto dto, Car car) {
        return Detail.builder()
                .car(car)
                .engineNumber(dto.engineNumber())
                .registrationCode(dto.registrationCode())
                .fuelType(dto.fuelType())
                .engineCapacity(dto.engineCapacity())
                .color(dto.color())
                .insuranceNumber(dto.insuranceNumber())
                .build();
    }
}
