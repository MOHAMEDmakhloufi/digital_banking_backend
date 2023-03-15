package com.fsb.digital_banking_backend.services;


import com.fsb.digital_banking_backend.entities.AppRole;
import com.fsb.digital_banking_backend.entities.AppUser;
import com.fsb.digital_banking_backend.repositories.AppRoleDao;
import com.fsb.digital_banking_backend.repositories.AppUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    @Autowired
    AppUserDao appUserDao;
    @Autowired
    AppRoleDao appRoleDao;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public AppUser addNewUser(AppUser appUser) {
        String password= appUser.getPassword();
        appUser.setPassword(passwordEncoder.encode(password));
        return appUserDao.save(appUser);
    }

    @Override
    public AppRole addNewRole(AppRole appRole) {
        return appRoleDao.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        AppUser appUser= appUserDao.findAppUserByUsername(username);
        AppRole appRole= appRoleDao.findAppRoleByRoleName(roleName);
        appUser.getRoles().add(appRole);
        
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserDao.findAppUserByUsername(username);
    }

    @Override
    public List<AppUser> listUsers() {
        return appUserDao.findAll();
    }
}
