package com.automotive.controller;

import com.automotive.dto.ReqCarDto;
import com.automotive.dto.RespCarDto;
import com.automotive.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping
    public List<RespCarDto> getCars() {
        return carService.getCars();
    }

    @GetMapping("/{id}")
    public RespCarDto getCarById(@PathVariable Integer id) {
        return carService.getCarById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addCar(@Valid @RequestBody ReqCarDto reqCarDto) {
        carService.addCar(reqCarDto);
    }

    @PutMapping("/{id}")
    public void updateCar(@PathVariable Integer id, @Valid @RequestBody ReqCarDto reqCarDto) {
        carService.updateCar(id, reqCarDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable Integer id) {
        carService.deleteCarById(id);
    }
}
