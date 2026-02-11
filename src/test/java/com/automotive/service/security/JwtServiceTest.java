package com.automotive.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    
    private JwtService jwtService;
    
    @BeforeEach
    void setUp () throws Exception {
        jwtService = new JwtService();
        setField("secret", "automotive-maintenance-jwt-test-key-2026");
        setField("accessExp", 3600000L);
        setField("refreshExp", 86400000L);
    }
    
    private void setField (String name, Object value) throws Exception {
        Field field = JwtService.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(jwtService, value);
    }
    
    private UserDetails testUser () {
        return new User("testuser", "password",
                        List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
    
    @Test
    void accessToken_shouldGenerateValidToken () {
        String token = jwtService.accessToken(testUser());
        
        assertNotNull(token);
        assertFalse(token.isBlank());
    }
    
    @Test
    void refreshToken_shouldGenerateValidToken () {
        String token = jwtService.refreshToken(testUser());
        
        assertNotNull(token);
        assertFalse(token.isBlank());
    }
    
    @Test
    void parse_shouldReturnCorrectClaims_forAccessToken () {
        String token = jwtService.accessToken(testUser());
        
        Claims claims = jwtService.parse(token);
        
        assertEquals("testuser", claims.getSubject());
        assertEquals("automotive", claims.getIssuer());
        assertEquals("access", claims.get("type"));
    }
    
    @Test
    void parse_shouldReturnCorrectClaims_forRefreshToken () {
        String token = jwtService.refreshToken(testUser());
        
        Claims claims = jwtService.parse(token);
        
        assertEquals("testuser", claims.getSubject());
        assertEquals("refresh", claims.get("type"));
    }
    
    @Test
    @SuppressWarnings("unchecked")
    void parse_shouldContainAuthorities () {
        String token = jwtService.accessToken(testUser());
        
        Claims claims = jwtService.parse(token);
        List<String> auths = claims.get("auth", List.class);
        
        assertNotNull(auths);
        assertTrue(auths.contains("ROLE_USER"));
    }
    
    @Test
    void parse_shouldThrowException_forInvalidToken () {
        assertThrows(Exception.class, () -> jwtService.parse("invalid.token.here"));
    }
    
    @Test
    void parse_shouldThrowException_forExpiredToken () throws Exception {
        setField("accessExp", 0L);
        String token = jwtService.accessToken(testUser());
        
        assertThrows(ExpiredJwtException.class, () -> jwtService.parse(token));
    }
}
