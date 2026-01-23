package com.automotive.service;

import com.automotive.dto.ModelDto;
import com.automotive.dto.ReqBrandDto;
import com.automotive.dto.RespBrandDto;
import com.automotive.exception.CarErrorEnum;
import com.automotive.exception.CarException;
import com.automotive.model.Brand;
import com.automotive.model.Model;
import com.automotive.repository.BrandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RespBrandDto> getBrands() {
        return brandRepository.findAll()
                .stream()
                .map(BrandServiceImpl::buildBrand)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public RespBrandDto getBrandById(int id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new CarException(CarErrorEnum.BRAND_NOT_FOUND));
        return buildBrand(brand);
    }

    @Override
    @Transactional
    public void addBrand(ReqBrandDto brandDto) {
        var brand = Brand.builder()
                .name(brandDto.name())
                .country(brandDto.country())
                .foundedYear(brandDto.foundedYear())
                .build();
        var models = brandDto.models().stream()
                .map(modelDto -> Model.builder()
                        .brand(brand)
                        .category(modelDto.category())
                        .name(modelDto.name())
                        .yearFrom(modelDto.yearFrom())
                        .yearTo(modelDto.yearTo())
                        .build()).toList();
        brand.setModels(models);
        brandRepository.save(brand);
    }

    @Override
    @Transactional
    public void deleteBrandById(int id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new CarException(CarErrorEnum.BRAND_NOT_FOUND));

        brandRepository.delete(brand);
    }


    @Override
    @Transactional
    public void updateBrand(int id, ReqBrandDto brandDto) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new CarException(CarErrorEnum.BRAND_NOT_FOUND));
        brand.setName(brandDto.name());
        brand.setCountry(brandDto.country());
        brandRepository.save(brand);
    }

    private static RespBrandDto buildBrand(Brand brand) {
        List<ModelDto> models = brand.getModels() == null
                ? List.of()
                : brand.getModels().stream()
                .filter(Objects::nonNull)
                .map(model -> new ModelDto(
                        model.getId(),
                        model.getName(),
                        model.getCategory(),
                        model.getYearFrom(),
                        model.getYearTo()
                ))
                .toList();

        return new RespBrandDto(
                brand.getId(),
                brand.getName(),
                brand.getCountry(),
                models
        );
    }


}