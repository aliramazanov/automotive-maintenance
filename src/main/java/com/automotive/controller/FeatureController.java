package com.automotive.controller;

import com.automotive.dto.FeatureDto;
import com.automotive.dto.ReqFeatureDto;
import com.automotive.service.FeatureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/features")
@RequiredArgsConstructor
public class FeatureController {

    private final FeatureService featureService;

    @GetMapping
    public List<FeatureDto> getFeatures() {
        return featureService.getFeatures();
    }

    @GetMapping("/{id}")
    public FeatureDto getFeatureById(@PathVariable Integer id) {
        return featureService.getFeatureById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addFeature(@Valid @RequestBody ReqFeatureDto reqFeatureDto) {
        featureService.addFeature(reqFeatureDto);
    }

    @PutMapping("/{id}")
    public void updateFeature(@PathVariable Integer id, @Valid @RequestBody ReqFeatureDto reqFeatureDto) {
        featureService.updateFeature(id, reqFeatureDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFeature(@PathVariable Integer id) {
        featureService.deleteFeatureById(id);
    }
}
