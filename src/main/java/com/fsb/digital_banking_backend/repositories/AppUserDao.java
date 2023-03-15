package com.fsb.digital_banking_backend.repositories;


import com.fsb.digital_banking_backend.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserDao extends JpaRepository<AppUser, Long> {
    AppUser findAppUserByUsername(String username);
}
