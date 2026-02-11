package com.automotive.service.security;

import com.automotive.exception.AuthErrorEnum;
import com.automotive.exception.AuthException;
import com.automotive.model.AppUser;
import com.automotive.repository.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    @Override
    public @NonNull UserDetails loadUserByUsername (@NonNull String username) {
        
        AppUser user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new AuthException(AuthErrorEnum.USER_NOT_EXISTS));
        
        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new DisabledException("User inactive");
        }
        
        Set<GrantedAuthority> authorities = buildAuthorities(user);
        
        return new User(user.getUsername(), user.getPasswordHash(), authorities);
    }
    
    private Set<GrantedAuthority> buildAuthorities (AppUser user) {
       
        Set<GrantedAuthority> authorities = new HashSet<>();
        
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            
            role.getPermissions().forEach(
                    permission -> authorities.add(new SimpleGrantedAuthority(permission.getPermissionCode()))
            );
        });
        
        return authorities;
    }
}
