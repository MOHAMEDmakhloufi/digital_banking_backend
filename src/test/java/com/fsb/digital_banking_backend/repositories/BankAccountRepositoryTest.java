package com.fsb.digital_banking_backend.repositories;

import com.fsb.digital_banking_backend.entities.BankAccount;
import com.fsb.digital_banking_backend.entities.CurrentAccount;
import com.fsb.digital_banking_backend.entities.Customer;
import com.fsb.digital_banking_backend.entities.SavingAccount;
import com.fsb.digital_banking_backend.enums.AccountStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
class BankAccountRepositoryTest {
    @Autowired
    private BankAccountRepository underTest;
    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void itShouldSaveCurrentAccount() {
        //GIVEN
        Customer customer = new Customer(1L, "Hassan", "Hassan@gmail.com", null);
        Customer savedCustomer = customerRepository.save(customer);
        BankAccount currentAccount = new CurrentAccount(9000);
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setBalance(Math.random()*90000);
        currentAccount.setCreateAt(new Date());
        currentAccount.setStatus(AccountStatus.CREATED);
        currentAccount.setCustomer(savedCustomer);


        //WHEN
        BankAccount currentAccountSaved = underTest.save(currentAccount);
        //THEN
        assertThat(currentAccountSaved)
                .isEqualToComparingFieldByField(currentAccount);
        assertThat(currentAccountSaved.getStatus()).isEqualTo(AccountStatus.CREATED);
    }

    @Test
    void itShouldSaveSavingAccount() {
        //GIVEN
        Customer customer = new Customer(1L, "Hassan", "Hassan@gmail.com", null);
        Customer savedCustomer = customerRepository.save(customer);
        BankAccount savingAccount = new SavingAccount(9000);
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setBalance(Math.random()*90000);
        savingAccount.setCreateAt(new Date());
        savingAccount.setStatus(AccountStatus.CREATED);
        savingAccount.setCustomer(savedCustomer);

        underTest.save(savingAccount);

        //WHEN
        BankAccount savingAccountSaved = underTest.save(savingAccount);
        //THEN
        assertThat(savingAccountSaved)
                .isEqualToComparingFieldByField(savingAccount);
        assertThat(savingAccountSaved.getStatus()).isEqualTo(AccountStatus.CREATED);
    }
}