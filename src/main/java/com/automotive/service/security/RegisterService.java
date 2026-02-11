package com.automotive.service.security;

import com.automotive.dto.security.RegisterRequestDto;
import com.automotive.exception.AuthErrorEnum;
import com.automotive.exception.AuthException;
import com.automotive.model.AppUser;
import com.automotive.model.Role;
import com.automotive.repository.security.RoleRepository;
import com.automotive.repository.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RegisterService {
    
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;
    
    @Transactional
    public void register (RegisterRequestDto req) {
        if (userRepo.findByUsername(req.username()).isPresent()) {
            throw new AuthException(AuthErrorEnum.USERNAME_EXISTS);
        }
        
        Role userRole = roleRepo
                .findByName("USER")
                .orElseThrow(() -> new AuthException(AuthErrorEnum.ROLE_NOT_FOUND));
        
        AppUser user = AppUser.builder()
                .username(req.username())
                .fullName(req.fullName())
                .email(req.email())
                .passwordHash(encoder.encode(req.password()))
                .active(true)
                .createdAt(LocalDateTime.now())
                .roles(Set.of(userRole))
                .build();
        
        userRepo.save(user);
    }
}
