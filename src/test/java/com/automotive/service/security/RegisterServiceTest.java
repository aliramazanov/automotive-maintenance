package com.automotive.service.security;

import com.automotive.dto.security.RegisterRequestDto;
import com.automotive.exception.AuthException;
import com.automotive.model.AppUser;
import com.automotive.model.Role;
import com.automotive.repository.security.RoleRepository;
import com.automotive.repository.security.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {
    
    @Mock
    private UserRepository userRepo;
    
    @Mock
    private RoleRepository roleRepo;
    
    @Mock
    private PasswordEncoder encoder;
    
    @InjectMocks
    private RegisterService registerService;
    
    private RegisterRequestDto request;
    private Role userRole;
    
    @BeforeEach
    void setUp () {
        request = new RegisterRequestDto("newuser", "New User", "new@test.com", "password123");
        userRole = Role.builder().id(1).name("USER").build();
    }
    
    @Test
    void register_shouldSaveUser () {
        when(userRepo.findByUsername("newuser")).thenReturn(Optional.empty());
        when(roleRepo.findByName("USER")).thenReturn(Optional.of(userRole));
        when(encoder.encode("password123")).thenReturn("encoded");
        
        registerService.register(request);
        
        verify(userRepo, times(1)).save(any(AppUser.class));
    }
    
    @Test
    void register_shouldThrowException_whenUsernameExists () {
        when(userRepo.findByUsername("newuser")).thenReturn(Optional.of(new AppUser()));
        
        assertThrows(AuthException.class, () -> registerService.register(request));
        verify(userRepo, never()).save(any());
    }
    
    @Test
    void register_shouldThrowException_whenRoleNotFound () {
        when(userRepo.findByUsername("newuser")).thenReturn(Optional.empty());
        when(roleRepo.findByName("USER")).thenReturn(Optional.empty());
        
        assertThrows(AuthException.class, () -> registerService.register(request));
        verify(userRepo, never()).save(any());
    }
    
    @Test
    void register_shouldEncodePassword () {
        when(userRepo.findByUsername("newuser")).thenReturn(Optional.empty());
        when(roleRepo.findByName("USER")).thenReturn(Optional.of(userRole));
        when(encoder.encode("password123")).thenReturn("encoded");
        
        registerService.register(request);
        
        verify(encoder, times(1)).encode("password123");
    }
}
