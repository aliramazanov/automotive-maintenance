package com.automotive.service;

import com.automotive.dto.ReqBrandDto;
import com.automotive.dto.RespBrandDto;

import java.util.List;

public interface BrandService {

    List<RespBrandDto> getBrands();

    RespBrandDto getBrandById(int id);

    void addBrand(ReqBrandDto car);

    void deleteBrandById(int id);

    void updateBrand(int id, ReqBrandDto carDto);
}
