package com.automotive.service;

import com.automotive.dto.ReqModelDto;
import com.automotive.dto.RespModelDto;
import com.automotive.exception.CarErrorEnum;
import com.automotive.exception.CarException;
import com.automotive.model.Brand;
import com.automotive.model.Model;
import com.automotive.repository.BrandRepository;
import com.automotive.repository.ModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModelServiceImpl implements ModelService {

    private final ModelRepository modelRepository;
    private final BrandRepository brandRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RespModelDto> getModels() {
        return modelRepository.findAll()
                .stream()
                .map(ModelServiceImpl::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RespModelDto getModelById(int id) {
        Model model = modelRepository.findById(id)
                .orElseThrow(() -> new CarException(CarErrorEnum.MODEL_NOT_FOUND));
        return toDto(model);
    }

    @Override
    @Transactional
    public void addModel(ReqModelDto modelDto) {
        Brand brand = brandRepository.findById(modelDto.brandId())
                .orElseThrow(() -> new CarException(CarErrorEnum.BRAND_NOT_FOUND));

        Model model = toEntity(modelDto, brand);
        modelRepository.save(model);
    }

    @Override
    @Transactional
    public void deleteModelById(int id) {
        Model model = modelRepository.findById(id)
                .orElseThrow(() -> new CarException(CarErrorEnum.MODEL_NOT_FOUND));
        modelRepository.delete(model);
    }

    @Override
    @Transactional
    public void updateModel(int id, ReqModelDto modelDto) {
        Model model = modelRepository.findById(id)
                .orElseThrow(() -> new CarException(CarErrorEnum.MODEL_NOT_FOUND));

        if (modelDto.brandId() != null && !modelDto.brandId().equals(model.getBrand().getId())) {
            Brand brand = brandRepository.findById(modelDto.brandId())
                    .orElseThrow(() -> new CarException(CarErrorEnum.BRAND_NOT_FOUND));
            model.setBrand(brand);
        }

        model.setName(modelDto.name());
        model.setCategory(modelDto.category());
        model.setYearFrom(modelDto.yearFrom());
        model.setYearTo(modelDto.yearTo());

        modelRepository.save(model);
    }

    private static RespModelDto toDto(Model model) {
        return new RespModelDto(
                model.getId(),
                model.getName(),
                model.getCategory(),
                model.getYearFrom(),
                model.getYearTo(),
                model.getBrand().getId(),
                model.getBrand().getName()
        );
    }

    private static Model toEntity(ReqModelDto dto, Brand brand) {
        return Model.builder()
                .brand(brand)
                .name(dto.name())
                .category(dto.category())
                .yearFrom(dto.yearFrom())
                .yearTo(dto.yearTo())
                .build();
    }
}
