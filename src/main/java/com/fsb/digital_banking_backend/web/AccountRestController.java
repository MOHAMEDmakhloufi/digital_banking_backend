package com.fsb.digital_banking_backend.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsb.digital_banking_backend.entities.AppRole;
import com.fsb.digital_banking_backend.entities.AppUser;
import com.fsb.digital_banking_backend.models.RoleUserForm;
import com.fsb.digital_banking_backend.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController

public class AccountRestController {
    @Autowired
    AccountService accountService;
    @GetMapping("users")
    @PostAuthorize("hasAuthority('USER')")
    public List<AppUser> findAllUsers(){
        return accountService.listUsers();
    }
    @PostMapping("users")
    @PostAuthorize("hasAuthority('ADMIN')")
    public AppUser saveUser(@RequestBody AppUser appUser){
        return accountService.addNewUser(appUser);
    }
    @PostMapping("roles")
    @PostAuthorize("hasAuthority('ADMIN')")
    public AppRole saveRole(@RequestBody AppRole appRole){
        return accountService.addNewRole(appRole);
    }
    @PostMapping("addRoleToUser")
    public void addRoleToUser(@RequestBody RoleUserForm roleUserForm){
        accountService.addRoleToUser(roleUserForm.getUsername(), roleUserForm.getRolename());
    }
    @GetMapping("/refreshToken")
    public void refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

        String authorizationToken= httpServletRequest.getHeader("Authorization");

        if(authorizationToken!=null && authorizationToken.startsWith("Bearer ")){
            try{
                String jwt= authorizationToken.substring(7);

                Algorithm algorithm= Algorithm.HMAC256("mySecretBank");
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(jwt);

                String username= decodedJWT.getSubject();
                AppUser user= accountService.loadUserByUsername(username);
                String jwtAccessToken= JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis()+1*60*1000))
                        .withIssuer(httpServletRequest.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(r -> r.getRoleName()).collect(Collectors.toList()))
                        .sign(algorithm );

                Map<String, String> idToken= new HashMap<>();
                idToken.put("access_token", jwtAccessToken);
                idToken.put("refresh_token", jwt);
                httpServletResponse.setContentType("application/json");
                new ObjectMapper().writeValue(httpServletResponse.getOutputStream(), idToken);
            }catch (Exception e){
                throw e;
            }
        }else {
            throw new RuntimeException("Refresh token required !!!!");
        }
    }
}
