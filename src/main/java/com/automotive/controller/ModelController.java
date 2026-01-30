package com.automotive.controller;

import com.automotive.dto.ReqModelDto;
import com.automotive.dto.RespModelDto;
import com.automotive.service.ModelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/models")
@RequiredArgsConstructor
public class ModelController {

    private final ModelService modelService;

    @GetMapping
    public List<RespModelDto> getModels() {
        return modelService.getModels();
    }

    @GetMapping("/{id}")
    public RespModelDto getModelById(@PathVariable Integer id) {
        return modelService.getModelById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addModel(@Valid @RequestBody ReqModelDto reqModelDto) {
        modelService.addModel(reqModelDto);
    }

    @PutMapping("/{id}")
    public void updateModel(@PathVariable Integer id, @Valid @RequestBody ReqModelDto reqModelDto) {
        modelService.updateModel(id, reqModelDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteModel(@PathVariable Integer id) {
        modelService.deleteModelById(id);
    }
}
