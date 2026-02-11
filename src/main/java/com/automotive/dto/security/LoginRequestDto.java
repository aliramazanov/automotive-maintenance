package com.automotive.dto.security;

import com.automotive.annotation.LogIgnore;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = "username is required")
        String username,
        
        @LogIgnore
        @NotBlank(message = "password is required")
        String password
) {
}
