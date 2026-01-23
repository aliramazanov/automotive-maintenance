package com.automotive.controller;

import com.automotive.dto.ReqBrandDto;
import com.automotive.dto.RespBrandDto;
import com.automotive.service.BrandService;
import com.automotive.validation.BrandGroupA;
import com.automotive.validation.BrandGroupB;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping("/brands")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<RespBrandDto> getBrands() {
        return brandService.getBrands();
    }

    @GetMapping("/brands/by-id")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RespBrandDto getBrandById(@RequestParam(value = "id") Integer id) {
        return brandService.getBrandById(id);
    }

    @PostMapping("/brands")
    public void addBrand(@RequestBody @Validated(value = BrandGroupA.class) @Valid ReqBrandDto reqBrandDto) {
        brandService.addBrand(reqBrandDto);
    }

    @PostMapping("/brands/groupb")
    public void addBrandGroupB(@RequestBody @Validated(value = BrandGroupB.class) @Valid ReqBrandDto reqBrandDto) {
        brandService.addBrand(reqBrandDto);
    }
}
