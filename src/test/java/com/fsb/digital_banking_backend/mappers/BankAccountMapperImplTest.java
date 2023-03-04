package com.fsb.digital_banking_backend.mappers;

import com.fsb.digital_banking_backend.dtos.AccountOperationDTO;
import com.fsb.digital_banking_backend.dtos.CurrentBankAccountDTO;
import com.fsb.digital_banking_backend.dtos.CustomerDTO;
import com.fsb.digital_banking_backend.dtos.SavingBankAccountDTO;
import com.fsb.digital_banking_backend.entities.*;
import com.fsb.digital_banking_backend.enums.AccountStatus;
import com.fsb.digital_banking_backend.enums.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BankAccountMapperImplTest {

    private BankAccountMapperImpl underTest;

    @BeforeEach
    void setUp() {
        underTest= new BankAccountMapperImpl();
    }

    @Test
    void itShouldFromCustomer() {
        //GIVEN
        Customer customer = new Customer(1L,
                "mohamed",
                "mohamed@gmail.com",
                List.of(new SavingAccount(), new CurrentAccount()));
        //WHEN
        CustomerDTO customerDTO = underTest.fromCustomer(customer);
        //THEN
        assertThat(customerDTO).isEqualToIgnoringGivenFields(customer, "bankAccounts");
    }

    @Test
    void itShouldFromCustomerDTO() {
        //GIVEN
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("mohamed");
        customerDTO.setEmail("med@gmail.com");
        //WHEN
        Customer customer = underTest.fromCustomerDTO(customerDTO);
        //THEN
        assertThat(customer).isEqualToIgnoringGivenFields(customerDTO, "bankAccounts");
    }

    @Test
    void itShouldFromSavingBankAccount() {
        //GIVEN
        Customer customer = new Customer(1L,
                "mohamed",
                "mohamed@gmail.com",
                List.of(new SavingAccount(), new CurrentAccount()));

        SavingAccount bankAccount= new SavingAccount(500);
        bankAccount.setId(UUID.randomUUID() .toString());
        bankAccount.setCreateAt(new Date());
        bankAccount.setBalance(5000) ;
        bankAccount.setCustomer(customer);
        //WHEN
        SavingBankAccountDTO savingBankAccountDTO = underTest.fromSavingBankAccount(bankAccount);
        CustomerDTO customerDTO = underTest.fromCustomer(customer);
        //THEN
        assertThat(savingBankAccountDTO).isEqualToIgnoringGivenFields(bankAccount, "type","accountOperations", "customerDTO");
        assertThat(savingBankAccountDTO.getType()).isEqualTo(bankAccount.getClass().getSimpleName());
        assertThat(savingBankAccountDTO.getCustomerDTO()).isEqualTo(customerDTO);
    }

    @Test
    void itShouldFromSavingBankAccountDTO() {
        //GIVEN
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("mohamed");
        customerDTO.setEmail("med@gmail.com");

        SavingBankAccountDTO bankAccountDTO= new SavingBankAccountDTO();
        bankAccountDTO.setId(UUID.randomUUID() .toString());
        bankAccountDTO.setCreateAt(new Date());
        bankAccountDTO.setBalance(5000) ;
        bankAccountDTO.setCustomerDTO(customerDTO);
        bankAccountDTO.setType(bankAccountDTO.getClass().getSimpleName());
        bankAccountDTO.setStatus(AccountStatus.CREATED);
        //WHEN
        SavingAccount savingBankAccount = underTest.fromSavingBankAccountDTO(bankAccountDTO);
        Customer customer = underTest.fromCustomerDTO(customerDTO);
        //THEN
        assertThat(savingBankAccount).isEqualToIgnoringGivenFields(bankAccountDTO, "accountOperations", "customer");
        assertThat(savingBankAccount.getCustomer()).isEqualTo(customer);
    }

    @Test
    void itShouldFromCurrentBankAccount() {
        //GIVEN
        Customer customer = new Customer(1L,
                "mohamed",
                "mohamed@gmail.com",
                List.of(new SavingAccount(), new CurrentAccount()));

        CurrentAccount bankAccount= new CurrentAccount(500);
        bankAccount.setId(UUID.randomUUID() .toString());
        bankAccount.setCreateAt(new Date());
        bankAccount.setBalance(5000) ;
        bankAccount.setCustomer(customer);
        //WHEN
        CurrentBankAccountDTO currentBankAccountDTO = underTest.fromCurrentBankAccount(bankAccount);
        CustomerDTO customerDTO = underTest.fromCustomer(customer);
        //THEN
        assertThat(currentBankAccountDTO).isEqualToIgnoringGivenFields(bankAccount, "type","accountOperations", "customerDTO");
        assertThat(currentBankAccountDTO.getType()).isEqualTo(bankAccount.getClass().getSimpleName());
        assertThat(currentBankAccountDTO.getCustomerDTO()).isEqualTo(customerDTO);
    }

    @Test
    void itShouldFromCurrentBankAccountDTO() {
        //GIVEN
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("mohamed");
        customerDTO.setEmail("med@gmail.com");

        CurrentBankAccountDTO bankAccountDTO= new CurrentBankAccountDTO();
        bankAccountDTO.setId(UUID.randomUUID() .toString());
        bankAccountDTO.setCreateAt(new Date());
        bankAccountDTO.setBalance(5000) ;
        bankAccountDTO.setCustomerDTO(customerDTO);
        bankAccountDTO.setType(bankAccountDTO.getClass().getSimpleName());
        bankAccountDTO.setStatus(AccountStatus.CREATED);
        bankAccountDTO.setOverDraft(200);
        //WHEN
        CurrentAccount currentBankAccount = underTest.fromCurrentBankAccountDTO(bankAccountDTO);
        Customer customer = underTest.fromCustomerDTO(customerDTO);
        //THEN
        assertThat(currentBankAccount).isEqualToIgnoringGivenFields(bankAccountDTO, "accountOperations", "customer");
        assertThat(currentBankAccount.getCustomer()).isEqualTo(customer);
    }

    @Test
    void itShouldFromAccountOperation() {
        //GIVEN
        Customer customer = new Customer(1L,
                "mohamed",
                "mohamed@gmail.com",
                List.of(new SavingAccount(), new CurrentAccount()));

        CurrentAccount bankAccount= new CurrentAccount(500);
        bankAccount.setId(UUID.randomUUID() .toString());
        bankAccount.setCreateAt(new Date());
        bankAccount.setBalance(5000) ;
        bankAccount.setCustomer(customer);
        AccountOperation accountOperation = new AccountOperation(1L,
                new Date(),
                490,
                OperationType.DEBIT,
                bankAccount,
                "my first operation");
        //WHEN
        AccountOperationDTO accountOperationDTO = underTest.fromAccountOperation(accountOperation);

        //THEN
        assertThat(accountOperationDTO).isEqualToComparingFieldByField(accountOperation);
    }
}