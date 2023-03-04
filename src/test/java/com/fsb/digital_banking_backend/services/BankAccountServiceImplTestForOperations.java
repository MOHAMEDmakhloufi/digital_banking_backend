package com.fsb.digital_banking_backend.services;

import com.fsb.digital_banking_backend.entities.AccountOperation;
import com.fsb.digital_banking_backend.entities.BankAccount;
import com.fsb.digital_banking_backend.entities.CurrentAccount;
import com.fsb.digital_banking_backend.exceptions.AmountNotVallidException;
import com.fsb.digital_banking_backend.exceptions.BalanceNotSufficientException;
import com.fsb.digital_banking_backend.exceptions.BankAccountNotFoundException;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class BankAccountServiceImplTestForOperations {
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
    void itShouldDebit() throws BankAccountNotFoundException, BalanceNotSufficientException, AmountNotVallidException {
        //GIVEN
        String bankId= UUID.randomUUID().toString();
        double amount = 1000;
        String description = "debit description";
        BankAccount bankAccount= new CurrentAccount(200);
        bankAccount.setId(bankId);
        bankAccount.setCreateAt(new Date());
        bankAccount.setBalance(3000) ;
        given(bankAccountRepository.findById(bankId))
                .willReturn(Optional.of(bankAccount));
        //WHEN
        underTest.debit(bankId, amount, description);
        //THEN
        ArgumentCaptor<AccountOperation> accountOperationArgumentCaptor = ArgumentCaptor.forClass(AccountOperation.class);
        then(accountOperationRepository).should().save(accountOperationArgumentCaptor.capture());
        ArgumentCaptor<BankAccount> bankAccountArgumentCaptor = ArgumentCaptor.forClass(BankAccount.class);
        then(bankAccountRepository).should().save(bankAccountArgumentCaptor.capture());

        assertThat(bankAccountArgumentCaptor.getValue().getBalance()).isEqualTo(2000);
    }

    @Test
    void itShouldThrowWhenBalanceNotSufficientDebit() throws BankAccountNotFoundException, BalanceNotSufficientException {
        //GIVEN
        String bankId= UUID.randomUUID().toString();
        double amount = 1000;
        String description = "debit description";
        BankAccount bankAccount= new CurrentAccount(200);
        bankAccount.setId(bankId);
        bankAccount.setCreateAt(new Date());
        bankAccount.setBalance(600) ;
        given(bankAccountRepository.findById(bankId))
                .willReturn(Optional.of(bankAccount));
        //WHEN
        //THEN
        assertThatThrownBy(()->underTest.debit(bankId, amount, description))
                .isInstanceOf(BalanceNotSufficientException.class)
                .hasMessageContaining("Balance not sufficient");

    }
    @ParameterizedTest
    @CsvSource(value = {"-1000", "0"} )
    void itShouldThrowWhenNotValidAmountDebit(double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        //GIVEN
        String bankId= UUID.randomUUID().toString();
        String description = "debit description";
        BankAccount bankAccount= new CurrentAccount(200);
        bankAccount.setId(bankId);
        bankAccount.setCreateAt(new Date());
        bankAccount.setBalance(3000) ;
        given(bankAccountRepository.findById(bankId))
                .willReturn(Optional.of(bankAccount));
        //WHEN
        //THEN
        assertThatThrownBy(()->underTest.debit(bankId, amount, description))
                .isInstanceOf(AmountNotVallidException.class);
        then(bankAccountRepository).shouldHaveNoInteractions();
        then(accountOperationRepository).shouldHaveNoInteractions();

    }
    @Test
    void itShouldCredit() throws BankAccountNotFoundException {
        //GIVEN
        String bankId= UUID.randomUUID().toString();
        double amount = 1000;
        String description = "debit description";
        BankAccount bankAccount= new CurrentAccount(200);
        bankAccount.setId(bankId);
        bankAccount.setCreateAt(new Date());
        bankAccount.setBalance(3000) ;
        given(bankAccountRepository.findById(bankId))
                .willReturn(Optional.of(bankAccount));
        //WHEN
        underTest.credit(bankId, amount, description);
        //THEN
        ArgumentCaptor<AccountOperation> accountOperationArgumentCaptor = ArgumentCaptor.forClass(AccountOperation.class);
        then(accountOperationRepository).should().save(accountOperationArgumentCaptor.capture());
        ArgumentCaptor<BankAccount> bankAccountArgumentCaptor = ArgumentCaptor.forClass(BankAccount.class);
        then(bankAccountRepository).should().save(bankAccountArgumentCaptor.capture());

        assertThat(bankAccountArgumentCaptor.getValue().getBalance()).isEqualTo(4000);


    }

    @Test
    void itShouldTransfer() {
        //GIVEN
        //WHEN
        //THEN
    }

    @Test
    void itShouldAccountHistory() {
        //GIVEN
        //WHEN
        //THEN
    }

    @Test
    void itShouldGetAccountHistory() {
        //GIVEN
        //WHEN
        //THEN
    }
}