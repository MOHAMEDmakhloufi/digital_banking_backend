package com.fsb.digital_banking_backend.services;

import com.fsb.digital_banking_backend.dtos.CustomerDTO;
import com.fsb.digital_banking_backend.entities.BankAccount;
import com.fsb.digital_banking_backend.entities.Customer;
import com.fsb.digital_banking_backend.exceptions.BalanceNotSufficientException;
import com.fsb.digital_banking_backend.exceptions.BankAccountNotFoundException;
import com.fsb.digital_banking_backend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

    CustomerDTO saveCustomer(CustomerDTO customerDTO) ;

    BankAccount saveBankAccount(double initialBalance, String type, double paramType, Long customerId)
            throws CustomerNotFoundException;

    List<CustomerDTO> ListCustomers();
    CustomerDTO getCustomerById(Long customerId) throws CustomerNotFoundException;

    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;

    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;

    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;

    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccount> bankAccountList();

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);
}
