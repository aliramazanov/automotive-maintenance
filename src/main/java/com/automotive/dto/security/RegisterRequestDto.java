package com.automotive.dto.security;

import com.automotive.annotation.LogIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(
        @NotBlank(message = "username is required")
        @Size(max = 50, message = "username can be max 50")
        String username,
        
        @Size(max = 100, message = "full name can be max 100")
        String fullName,
        
        @Email(message = "invalid email format")
        @Size(max = 100, message = "email can be max 100")
        String email,
        
        @LogIgnore
        @NotBlank(message = "password is required")
        @Size(min = 6, message = "password must be at least 6 characters")
        String password
) {
}
