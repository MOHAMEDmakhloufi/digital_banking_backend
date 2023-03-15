package com.fsb.digital_banking_backend.security;

import com.fsb.digital_banking_backend.entities.AppRole;
import com.fsb.digital_banking_backend.entities.AppUser;
import com.fsb.digital_banking_backend.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsServiceImp implements UserDetailsService {
    @Autowired
    AccountService accountService;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        AppUser appUser = accountService.loadUserByUsername(s);
        Collection<GrantedAuthority> authorityCollection = new ArrayList<>();
        appUser.getRoles().forEach(appRole -> {
            authorityCollection.add(new SimpleGrantedAuthority(appRole.getRoleName()));
        });
        return new User(appUser.getUsername(), appUser.getPassword(), authorityCollection);
    }
}
