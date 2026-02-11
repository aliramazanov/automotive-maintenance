package com.automotive.dto;

import com.automotive.annotation.LogIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReqDetailDto(
        @NotBlank(message = "required")
        @Size(max = 20, message = "engine number can be max 20")
        String engineNumber,
        
        @NotBlank(message = "required")
        @Size(max = 20, message = "registration code can be max 20")
        String registrationCode,
        
        @NotBlank(message = "required")
        @Size(max = 20, message = "fuel type can be max 20")
        String fuelType,
        
        @NotBlank(message = "required")
        @Size(max = 200, message = "engine capacity can be max 200")
        String engineCapacity,
        
        @Size(max = 255, message = "max 255")
        String color,
        
        @LogIgnore
        @Size(max = 255, message = "max 255")
        String insuranceNumber
) {
}
