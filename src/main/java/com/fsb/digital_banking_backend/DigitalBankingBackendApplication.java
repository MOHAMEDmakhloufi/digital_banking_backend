package com.fsb.digital_banking_backend;

import com.fsb.digital_banking_backend.entities.AccountOperation;
import com.fsb.digital_banking_backend.entities.CurrentAccount;
import com.fsb.digital_banking_backend.entities.Customer;
import com.fsb.digital_banking_backend.entities.SavingAccount;
import com.fsb.digital_banking_backend.enums.AccountStatus;
import com.fsb.digital_banking_backend.enums.OperationType;
import com.fsb.digital_banking_backend.repositories.AccountOperationRepository;
import com.fsb.digital_banking_backend.repositories.BankAccountRepository;
import com.fsb.digital_banking_backend.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitalBankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankingBackendApplication.class, args);
    }

}
