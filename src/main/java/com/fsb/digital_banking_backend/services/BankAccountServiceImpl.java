package com.fsb.digital_banking_backend.services;

import com.fsb.digital_banking_backend.dtos.CustomerDTO;
import com.fsb.digital_banking_backend.entities.*;
import com.fsb.digital_banking_backend.enums.OperationType;
import com.fsb.digital_banking_backend.exceptions.BalanceNotSufficientException;
import com.fsb.digital_banking_backend.exceptions.BankAccountNotFoundException;
import com.fsb.digital_banking_backend.exceptions.CustomerNotFoundException;
import com.fsb.digital_banking_backend.mappers.BankAccountMapperImpl;
import com.fsb.digital_banking_backend.repositories.AccountOperationRepository;
import com.fsb.digital_banking_backend.repositories.BankAccountRepository;
import com.fsb.digital_banking_backend.repositories.CustomerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
public class BankAccountServiceImpl implements BankAccountService {

    private final AccountOperationRepository accountOperationRepository;
    private final BankAccountRepository bankAccountRepository;
    private final CustomerRepository customerRepository;
    private final BankAccountMapperImpl bankAccountMapper;
    Logger log= LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    public BankAccountServiceImpl(AccountOperationRepository accountOperationRepository, BankAccountRepository bankAccountRepository, CustomerRepository customerRepository, BankAccountMapperImpl bankAccountMapper) {
        this.accountOperationRepository = accountOperationRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.customerRepository = customerRepository;
        this.bankAccountMapper = bankAccountMapper;
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        Customer customer = bankAccountMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer= customerRepository.save(customer);
        CustomerDTO customerDTO1 = bankAccountMapper.fromCustomer(savedCustomer);
        return customerDTO1;
    }

    @Override
    public BankAccount saveBankAccount(double initialBalance, String type, double paramType, Long customerId)
            throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(customerId).orElse(null);

        if (customer==null)
            throw new CustomerNotFoundException("Customer not found");
        BankAccount bankAccount;
        if(type.equals("current")){

            bankAccount=new CurrentAccount(paramType) ;
        } else {

            bankAccount = new SavingAccount(paramType);
        }
        bankAccount.setId(UUID.randomUUID() .toString());
        bankAccount.setCreateAt(new Date());
        bankAccount.setBalance(initialBalance) ;
        bankAccount.setCustomer(customer);
        BankAccount savedBank = bankAccountRepository.save(bankAccount);
        return savedBank;
    }

    @Override
    public List<CustomerDTO> ListCustomers() {
        List<Customer> customers = customerRepository.findAll();

        List<CustomerDTO> customersDTO = customers.stream().map(customer -> bankAccountMapper.fromCustomer(customer)).collect(Collectors.toList());

        return customersDTO;
    }

    @Override
    public CustomerDTO getCustomerById(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(()->new CustomerNotFoundException("Customer Not Found"));
        return bankAccountMapper.fromCustomer(customer);
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));
        return bankAccount;
    }

    @Override
    public void debit(String accountId, double amount, String description)
            throws BankAccountNotFoundException, BalanceNotSufficientException {

        BankAccount bankAccount=getBankAccount(accountId) ;
        if (bankAccount.getBalance()<amount)
            throw new BalanceNotSufficientException("Balance not sufficient");

        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setOperationType(OperationType.DEBIT);
        accountOperation.setAmount(amount) ;
        accountOperation.setDescription(description) ;
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount (bankAccount) ;
        accountOperationRepository.save(accountOperation) ;
        bankAccount.setBalance(bankAccount.getBalance()-amount) ;

        bankAccountRepository. save(bankAccount);

    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount=getBankAccount(accountId) ;

        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setOperationType(OperationType.CREDIT);
        accountOperation.setAmount(amount) ;
        accountOperation.setDescription(description) ;
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount (bankAccount) ;
        accountOperationRepository.save(accountOperation) ;
        bankAccount.setBalance(bankAccount.getBalance()+amount) ;

        bankAccountRepository. save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount)
            throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource, amount, "Transfer to "+accountIdDestination);
        credit(accountIdDestination, amount, "Transfer from "+accountIdSource);
    }

    @Override
    public List<BankAccount> bankAccountList() {
        return bankAccountRepository.findAll();
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        Customer customer = bankAccountMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer= customerRepository.save(customer);
        CustomerDTO customerDTO1 = bankAccountMapper.fromCustomer(savedCustomer);
        return customerDTO1;
    }
    @Override
    public void deleteCustomer(Long customerId){
        customerRepository.deleteById(customerId);
    }
}
