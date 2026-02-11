package com.automotive.repository.security;

import com.automotive.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Integer> {
    
    Optional<AppUser> findByUsername (String username);
}
