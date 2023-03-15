package com.fsb.digital_banking_backend;

import com.fsb.digital_banking_backend.entities.*;
import com.fsb.digital_banking_backend.enums.AccountStatus;
import com.fsb.digital_banking_backend.enums.OperationType;
import com.fsb.digital_banking_backend.repositories.AccountOperationRepository;
import com.fsb.digital_banking_backend.repositories.BankAccountRepository;
import com.fsb.digital_banking_backend.repositories.CustomerRepository;
import com.fsb.digital_banking_backend.security.UserDetailsServiceImp;
import com.fsb.digital_banking_backend.services.AccountService;
import com.fsb.digital_banking_backend.services.AccountServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.*;
import java.util.stream.Stream;

@SpringBootApplication
@CrossOrigin(origins = "*", allowedHeaders = "*", allowCredentials = "true", maxAge = 3600)
public class DigitalBankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankingBackendApplication.class, args);
    }
    @Bean
    PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
    @Bean
    UserDetailsService userDetailsService(){return new UserDetailsServiceImp();}
    @Bean
    AccountService accountService(){return new AccountServiceImpl();}
    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/*")
                        .allowedHeaders("*")
                        .allowedOrigins("*")
                        .allowedMethods("*")
                        .allowCredentials(true);
            }
        };
    }
    @Bean
    CommandLineRunner commandLineRunner(AccountService accountService){
        return args -> {

            accountService. addNewRole(new AppRole(null, "USER"));
            accountService. addNewRole(new AppRole( null, "ADMIN"));
            accountService. addNewRole(new AppRole (null, "CUSTOMER_MANAGER"));
            accountService. addNewRole(new AppRole(null, "PRODUCT_MANAGER"));
            accountService.addNewRole(new AppRole(null, "BILLS_MANAGER"));

            accountService. addNewUser(new AppUser(null, "user1", "1234", new ArrayList<>()));
            accountService. addNewUser(new AppUser (null, "admin", "1234", new ArrayList<>()));
            accountService. addNewUser(new AppUser (null, "user2", "1234", new ArrayList<>()));
            accountService. addNewUser(new AppUser (null, "user3", "1234", new ArrayList<>()));
            accountService. addNewUser(new AppUser (null, "user4", "1234",new ArrayList<>()));

            accountService.addRoleToUser("user1", "USER");
            accountService.addRoleToUser("admin", "USER");
            accountService.addRoleToUser("admin", "ADMIN");
            accountService.addRoleToUser("user2", "USER");
            accountService.addRoleToUser("user2", "CUSTOMER_MANAGER");
            accountService.addRoleToUser("user3", "USER");
            accountService.addRoleToUser("user3", "PRODUCT_MANAGER");
            accountService.addRoleToUser("user4", "USER");
            accountService.addRoleToUser("user2", "BILLS_MANAGER");


        };
    }
}
