package com.fsb.digital_banking_backend.services;

import com.fsb.digital_banking_backend.dtos.BankAccountDTO;
import com.fsb.digital_banking_backend.dtos.CurrentBankAccountDTO;
import com.fsb.digital_banking_backend.dtos.CustomerDTO;
import com.fsb.digital_banking_backend.dtos.SavingBankAccountDTO;
import com.fsb.digital_banking_backend.entities.*;
import com.fsb.digital_banking_backend.exceptions.BalanceNotSufficientException;
import com.fsb.digital_banking_backend.exceptions.BankAccountNotFoundException;
import com.fsb.digital_banking_backend.exceptions.CustomerNotFoundException;
import com.fsb.digital_banking_backend.mappers.BankAccountMapperImpl;
import com.fsb.digital_banking_backend.repositories.AccountOperationRepository;
import com.fsb.digital_banking_backend.repositories.BankAccountRepository;
import com.fsb.digital_banking_backend.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

class BankAccountServiceImplTest {
    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BankAccountMapperImpl bankAccountMapper;
    @Mock
    private AccountOperationRepository accountOperationRepository;
    private BankAccountService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest= new BankAccountServiceImpl(accountOperationRepository,
                bankAccountRepository,
                customerRepository,
                bankAccountMapper);
    }



    @Test
    void itShouldSaveCurrentAccountSuccessfully() throws CustomerNotFoundException {
        //GIVEN
        Long customerId= 1L;
        given(customerRepository.findById(customerId)).willReturn(Optional.of(mock(Customer.class)));

        double initialBalance=9000;
        String type="current";
        double paramType=500;
        //WHEN
        underTest.saveBankAccount(initialBalance,   type, paramType, customerId);
        //THEN
        ArgumentCaptor<BankAccount> bankAccountArgumentCaptor = ArgumentCaptor.forClass(BankAccount.class);
        then(bankAccountRepository).should().save(bankAccountArgumentCaptor.capture());
        BankAccount bankAccount= new CurrentAccount(paramType);
        bankAccount.setId(UUID.randomUUID() .toString());
        bankAccount.setCreateAt(new Date());
        bankAccount.setBalance(initialBalance) ;

        assertThat(bankAccountArgumentCaptor.getValue())
                .isInstanceOf(CurrentAccount.class)
                .isEqualToIgnoringGivenFields(bankAccount, "id", "customer", "createAt");
        assertThat(bankAccountArgumentCaptor.getValue().getId()).isNotNull();
        assertThat(bankAccountArgumentCaptor.getValue().getCreateAt()).isNotNull();
        assertThat(bankAccountArgumentCaptor.getValue().getCustomer()).isNotNull();
    }
    @Test
    void itShouldSaveSavingBankAccountSuccessfully() throws CustomerNotFoundException {
        //GIVEN
        Long customerId= 1L;
        given(customerRepository.findById(customerId)).willReturn(Optional.of(mock(Customer.class)));
        double initialBalance=9000;
        String type="SA";
        double paramType=500;
        //WHEN
        underTest.saveBankAccount(initialBalance,   type, paramType, customerId);
        //THEN
        ArgumentCaptor<BankAccount> bankAccountArgumentCaptor = ArgumentCaptor.forClass(BankAccount.class);
        then(bankAccountRepository).should().save(bankAccountArgumentCaptor.capture());
        BankAccount bankAccount= new SavingAccount(paramType);
        bankAccount.setId(UUID.randomUUID() .toString());
        bankAccount.setCreateAt(new Date());
        bankAccount.setBalance(initialBalance) ;

        assertThat(bankAccountArgumentCaptor.getValue())
                .isInstanceOf(SavingAccount.class)
                .isEqualToIgnoringGivenFields(bankAccount, "id", "customer", "createAt");
        assertThat(bankAccountArgumentCaptor.getValue().getId()).isNotNull();
        assertThat(bankAccountArgumentCaptor.getValue().getCreateAt()).isNotNull();
        assertThat(bankAccountArgumentCaptor.getValue().getCustomer()).isNotNull();
    }
    @Test
    void itShouldThrowWhenCustomerNotFound() throws CustomerNotFoundException {
        //GIVEN
        Long customerId= 1L;
        given(customerRepository.findById(customerId)).willReturn(Optional.empty());
        double initialBalance=9000;
        String type="SA";
        double paramType=500;
        //WHEN
        //THEN
        assertThatThrownBy(()->underTest.saveBankAccount(initialBalance,   type, paramType, customerId))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining("Customer not found");
        then(bankAccountRepository).should(never()).save(any(BankAccount.class));
    }


    @Test
    void itShouldGetBankAccount() throws BankAccountNotFoundException {
        //GIVEN
        String bankId= UUID.randomUUID().toString();
        given(bankAccountRepository.findById(bankId)).willReturn(Optional.of(mock(SavingAccount.class)));
        given(bankAccountMapper.fromSavingBankAccount(any())).willReturn(new SavingBankAccountDTO());
        given(bankAccountMapper.fromCurrentBankAccount(any())).willReturn(new CurrentBankAccountDTO());
        //WHEN
        BankAccountDTO bankAccount = underTest.getBankAccount(bankId);
        //THEN
        assertThat(bankAccount).isNotNull();
    }
    @Test
    void itShouldThrowWhenGetBankAccountAndIdNotExist() {
        //GIVEN
        String bankId= UUID.randomUUID().toString();
        given(bankAccountRepository.findById(bankId)).willReturn(Optional.empty());
        //WHEN
        //THEN
        assertThatThrownBy(()-> underTest.getBankAccount(bankId))
                .isInstanceOf(BankAccountNotFoundException.class)
                .hasMessageContaining("BankAccount not found");
    }




    @Test
    void itShouldTransfer() {
        //GIVEN
        //WHEN
        //THEN
    }
}