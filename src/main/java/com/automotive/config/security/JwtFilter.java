package com.automotive.config.security;

import com.automotive.service.security.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    
    private static final List<String> PUBLIC_PATHS = List.of(
            "/auth/login",
            "/auth/register",
            "/auth/refresh"
    );
    
    private final JwtService jwt;
    
    @Override
    protected boolean shouldNotFilter (@NonNull HttpServletRequest request) {
        String path = request.getServletPath();
        return PUBLIC_PATHS.contains(path);
    }
    
    @Override
    protected void doFilterInternal (
            @NonNull HttpServletRequest req,
            @NonNull HttpServletResponse res,
            @NonNull FilterChain chain
    ) throws IOException, ServletException {
        
        String token = extractToken(req);
        
        if (token == null) {
            chain.doFilter(req, res);
            return;
        }
        
        try {
            Claims claims = jwt.parse(token);
            
            if (!"access".equals(claims.get("type"))) {
                chain.doFilter(req, res);
                return;
            }
            
            setAuthentication(claims);
        }
        catch (Exception e) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }
        
        chain.doFilter(req, res);
    }
    
    private String extractToken (HttpServletRequest req) {
        String header = req.getHeader("Authorization");
      
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
      
        return null;
    }
    
    @SuppressWarnings("unchecked")
    private void setAuthentication (Claims claims) {
        String username = claims.getSubject();
        
        List<String> auths = claims.get("auth", List.class);
        
        List<GrantedAuthority> authorities = (auths != null)
                
                ? auths.stream()
                .map(SimpleGrantedAuthority::new)
                .map(GrantedAuthority.class::cast).toList()
                
                : Collections.emptyList();
        
        var auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
        
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
