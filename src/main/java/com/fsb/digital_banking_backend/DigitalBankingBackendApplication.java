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
    /*
    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository,
                                        BankAccountRepository bankAccountRepository,
                                        AccountOperationRepository accountOperationRepository){
        return args -> {
            Stream.of("Hassan", "Yassine", "Aicha").forEach(name ->{
                Customer customer= new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);

            });
            customerRepository.findAll().forEach(customer -> {
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*90000);
                currentAccount.setCreateAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(customer);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*90000);
                savingAccount.setCreateAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(customer);
                savingAccount.setInterestRate(9000);
                bankAccountRepository.save(savingAccount);
            });
            bankAccountRepository.findAll().forEach(bankAccount -> {
                for(int i=0; i<10;i++){
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*12000);
                    accountOperation.setOperationType(Math.random()>0.5? OperationType.DEBIT: OperationType.CREDIT);
                    accountOperation.setBankAccount(bankAccount);
                    accountOperationRepository.save(accountOperation);
                }
            });
        };


    }

     */
}
