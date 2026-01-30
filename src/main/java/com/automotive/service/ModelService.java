package com.automotive.service;

import com.automotive.dto.ReqModelDto;
import com.automotive.dto.RespModelDto;

import java.util.List;

public interface ModelService {

    List<RespModelDto> getModels();

    RespModelDto getModelById(int id);

    void addModel(ReqModelDto modelDto);

    void deleteModelById(int id);

    void updateModel(int id, ReqModelDto modelDto);
}
