package com.automotive.controller;

import com.automotive.dto.security.LoginRequestDto;
import com.automotive.dto.security.RefreshRequestDto;
import com.automotive.dto.security.RegisterRequestDto;
import com.automotive.dto.security.TokenResponseDto;
import com.automotive.exception.AuthErrorEnum;
import com.automotive.exception.AuthException;
import com.automotive.service.security.JwtService;
import com.automotive.service.security.RegisterService;
import com.automotive.service.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager manager;
    private final JwtService jwt;
    private final RegisterService registerService;
    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("/login")
    public TokenResponseDto login(@Valid @RequestBody LoginRequestDto req) {
        var auth = manager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        UserDetails user = (UserDetails) auth.getPrincipal();
        return buildTokenResponse(user);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequestDto req) {
        registerService.register(req);
    }

    @PostMapping("/refresh")
    public TokenResponseDto refresh(@Valid @RequestBody RefreshRequestDto req) {
        Claims claims = jwt.parse(req.refreshToken());

        if (!"refresh".equals(claims.get("type"))) {
            throw new AuthException(AuthErrorEnum.INVALID_TOKEN);
        }

        UserDetails user = userDetailsService.loadUserByUsername(claims.getSubject());
        return buildTokenResponse(user);
    }

    private TokenResponseDto buildTokenResponse(UserDetails user) {
        return new TokenResponseDto(jwt.accessToken(user), jwt.refreshToken(user));
    }
}
