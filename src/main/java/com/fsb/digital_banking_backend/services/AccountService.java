package com.fsb.digital_banking_backend.services;



import com.fsb.digital_banking_backend.entities.AppRole;
import com.fsb.digital_banking_backend.entities.AppUser;

import java.util.List;

public interface AccountService {
    AppUser addNewUser(AppUser appUser);
    AppRole addNewRole(AppRole appRole);
    void addRoleToUser(String username, String roleName);
    AppUser loadUserByUsername(String username);
    List<AppUser> listUsers();
}
