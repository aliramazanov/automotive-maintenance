package com.automotive.service.security;

import com.automotive.exception.AuthException;
import com.automotive.model.AppUser;
import com.automotive.model.Permission;
import com.automotive.model.Role;
import com.automotive.repository.security.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;
    
    private AppUser user;
    
    @BeforeEach
    void setUp () {
        Permission perm = Permission.builder().id(1).permissionCode("READ_CARS").build();
        Role role = Role.builder().id(1).name("USER").permissions(Set.of(perm)).build();
        user = AppUser.builder()
                .id(1)
                .username("testuser")
                .passwordHash("hashed")
                .active(true)
                .roles(Set.of(role))
                .build();
    }
    
    @Test
    void loadUserByUsername_shouldReturnUserDetails () {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        
        UserDetails result = userDetailsService.loadUserByUsername("testuser");
        
        assertEquals("testuser", result.getUsername());
        assertEquals("hashed", result.getPassword());
    }
    
    @Test
    void loadUserByUsername_shouldContainRoleAndPermission () {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        
        UserDetails result = userDetailsService.loadUserByUsername("testuser");
        
        var authorities = result.getAuthorities().stream()
                .map(Object::toString)
                .toList();
        assertTrue(authorities.contains("ROLE_USER"));
        assertTrue(authorities.contains("READ_CARS"));
    }
    
    @Test
    void loadUserByUsername_shouldThrowException_whenNotFound () {
        when(userRepository.findByUsername("nobody")).thenReturn(Optional.empty());
        
        assertThrows(AuthException.class, () -> userDetailsService.loadUserByUsername("nobody"));
    }
    
    @Test
    void loadUserByUsername_shouldThrowException_whenInactive () {
        user.setActive(false);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        
        assertThrows(DisabledException.class, () -> userDetailsService.loadUserByUsername("testuser"));
    }
    
    @Test
    void loadUserByUsername_shouldThrowException_whenActiveIsNull () {
        user.setActive(null);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        
        assertThrows(DisabledException.class, () -> userDetailsService.loadUserByUsername("testuser"));
    }
}
