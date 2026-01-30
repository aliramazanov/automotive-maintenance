package com.automotive.controller;

import com.automotive.dto.ReqBrandDto;
import com.automotive.dto.RespBrandDto;
import com.automotive.service.BrandService;
import com.automotive.validation.BrandGroupA;
import com.automotive.validation.BrandGroupB;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    public List<RespBrandDto> getBrands() {
        return brandService.getBrands();
    }

    @GetMapping("/{id}")
    public RespBrandDto getBrandById(@PathVariable Integer id) {
        return brandService.getBrandById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addBrand(@RequestBody @Validated(BrandGroupA.class) @Valid ReqBrandDto reqBrandDto) {
        brandService.addBrand(reqBrandDto);
    }

    @PostMapping("/groupb")
    @ResponseStatus(HttpStatus.CREATED)
    public void addBrandGroupB(@RequestBody @Validated(BrandGroupB.class) @Valid ReqBrandDto reqBrandDto) {
        brandService.addBrand(reqBrandDto);
    }

    @PutMapping("/{id}")
    public void updateBrand(@PathVariable Integer id, @Valid @RequestBody ReqBrandDto reqBrandDto) {
        brandService.updateBrand(id, reqBrandDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBrand(@PathVariable Integer id) {
        brandService.deleteBrandById(id);
    }
}
