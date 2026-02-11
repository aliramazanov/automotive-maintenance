package com.automotive.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.access-exp}")
    private long accessExp;
    
    @Value("${jwt.refresh-exp}")
    private long refreshExp;
    
    private SecretKey key () {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    
    private String buildToken (UserDetails user, long exp, String type) {
        List<String> auth = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        
        return Jwts.builder()
                .subject(user.getUsername())
                .issuer("automotive")
                .claim("auth", auth)
                .claim("type", type)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + exp))
                .signWith(key())
                .compact();
    }
    
    public String accessToken (UserDetails user) {
        return buildToken(user, accessExp, "access");
    }
    
    public String refreshToken (UserDetails user) {
        return buildToken(user, refreshExp, "refresh");
    }
    
    public Claims parse (String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
