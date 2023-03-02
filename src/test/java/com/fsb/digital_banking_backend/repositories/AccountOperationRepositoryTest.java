package com.fsb.digital_banking_backend.repositories;

import com.fsb.digital_banking_backend.entities.AccountOperation;
import com.fsb.digital_banking_backend.entities.BankAccount;
import com.fsb.digital_banking_backend.entities.CurrentAccount;
import com.fsb.digital_banking_backend.entities.Customer;
import com.fsb.digital_banking_backend.enums.AccountStatus;
import com.fsb.digital_banking_backend.enums.OperationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
class AccountOperationRepositoryTest {
    @Autowired
    private AccountOperationRepository underTest;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void itShouldSaveOperation() {
        //GIVEN
        Customer customer = new Customer(1L, "Hassan", "Hassan@gmail.com", null);
        customerRepository.save(customer);
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setBalance(Math.random()*90000);
        currentAccount.setCreateAt(new Date());
        currentAccount.setStatus(AccountStatus.ACTIVATED);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(9000);
        bankAccountRepository.save(currentAccount);

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setOperationDate(new Date());
        accountOperation.setAmount(Math.random()*12000);
        accountOperation.setOperationType(Math.random()>0.5? OperationType.DEBIT: OperationType.CREDIT);
        accountOperation.setBankAccount(currentAccount);


        //WHEN
        AccountOperation accountOperationSaved = underTest.save(accountOperation);
        //THEN
        assertThat( accountOperationSaved)
                .isEqualToComparingFieldByField(accountOperation);
    }
}