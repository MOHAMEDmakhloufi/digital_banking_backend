package com.fsb.digital_banking_backend.repositories;


import com.fsb.digital_banking_backend.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRoleDao extends JpaRepository<AppRole, Long> {
    AppRole findAppRoleByRoleName(String roleName);
}
