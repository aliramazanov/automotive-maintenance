package com.automotive.service;

import com.automotive.dto.ReqCarDto;
import com.automotive.dto.RespCarDto;

import java.util.List;

public interface CarService {

    List<RespCarDto> getCars();

    RespCarDto getCarById(int id);

    void addCar(ReqCarDto carDto);

    void deleteCarById(int id);

    void updateCar(int id, ReqCarDto carDto);
}
