package com.fsb.digital_banking_backend.services;

import com.fsb.digital_banking_backend.dtos.AccountHistoryDTO;
import com.fsb.digital_banking_backend.dtos.AccountOperationDTO;
import com.fsb.digital_banking_backend.dtos.BankAccountDTO;
import com.fsb.digital_banking_backend.dtos.CustomerDTO;
import com.fsb.digital_banking_backend.entities.BankAccount;
import com.fsb.digital_banking_backend.entities.Customer;
import com.fsb.digital_banking_backend.exceptions.AmountNotVallidException;
import com.fsb.digital_banking_backend.exceptions.BalanceNotSufficientException;
import com.fsb.digital_banking_backend.exceptions.BankAccountNotFoundException;
import com.fsb.digital_banking_backend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

    CustomerDTO saveCustomer(CustomerDTO customerDTO) ;
    List<CustomerDTO> listOfCustomers();
    CustomerDTO getCustomerById(Long customerId) throws CustomerNotFoundException;
    CustomerDTO updateCustomer(CustomerDTO customerDTO) throws CustomerNotFoundException;
    void deleteCustomer(Long customerId) throws CustomerNotFoundException;

    BankAccountDTO saveBankAccount(double initialBalance, String type, double paramType, Long customerId)
            throws CustomerNotFoundException;
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description)
            throws BankAccountNotFoundException, BalanceNotSufficientException, AmountNotVallidException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDestination, double amount)
            throws BankAccountNotFoundException, BalanceNotSufficientException, AmountNotVallidException;

    List<BankAccountDTO> bankAccountList();

    public List<AccountOperationDTO> accountHistory(String accountId);


    AccountHistoryDTO getAccountHistory(String accountId, int page, int size)
            throws BankAccountNotFoundException;

    List<CustomerDTO> searchCustomers(String keyWord);
}
