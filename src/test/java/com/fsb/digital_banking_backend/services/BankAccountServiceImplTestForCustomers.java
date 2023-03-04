package com.fsb.digital_banking_backend.services;

import com.fsb.digital_banking_backend.dtos.CustomerDTO;
import com.fsb.digital_banking_backend.entities.CurrentAccount;
import com.fsb.digital_banking_backend.entities.Customer;
import com.fsb.digital_banking_backend.entities.SavingAccount;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class BankAccountServiceImplTestForCustomers {
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
    void itShouldSaveCustomer() {
        //GIVEN
        CustomerDTO customer = new CustomerDTO();
        customer.setName("Hassan");
        customer.setEmail("Hassan@gmail.com");
        given(bankAccountMapper.fromCustomerDTO(customer)).willCallRealMethod();
        //WHEN
        underTest.saveCustomer(customer);
        //THEN
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        assertThat(customerArgumentCaptor.getValue()).isNotNull();
    }

    @Test
    void itShouldListCustomers() {
        //GIVEN
        Customer customer = new Customer(1L,
                "mohamed",
                "mohamed@gmail.com",
                List.of(new SavingAccount(), new CurrentAccount()));
        Customer customer2 = new Customer(2L,
                "ahmed",
                "ahmed@gmail.com",
                List.of(new SavingAccount()));
        given(customerRepository.findAll()).willReturn(List.of(customer, customer2));
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("mohamed");
        customerDTO.setEmail("mohamed@gmail.com");

        CustomerDTO customerDTO2 = new CustomerDTO();
        customerDTO.setId(2L);
        customerDTO.setName("ahmed");
        customerDTO.setEmail("ahmed@gmail.com");
        given(bankAccountMapper.fromCustomer(customer)).willReturn(customerDTO);
        given(bankAccountMapper.fromCustomer(customer2)).willReturn(customerDTO2);
        //WHEN
        List<CustomerDTO> customerDTOS = underTest.listOfCustomers();
        //THEN
        assertThat(customerDTOS).isNotEmpty();
        assertThat(customerDTOS).hasSize(2);
        assertThat(customerDTOS).contains(customerDTO, customerDTO2);
    }

    @Test
    void itShouldGetCustomerById() throws CustomerNotFoundException {
        //GIVEN
        long customerId= 1L;
        Customer customer = new Customer(customerId,
                "mohamed",
                "mohamed@gmail.com",
                List.of(new SavingAccount(), new CurrentAccount()));
        given(customerRepository.findById(customerId)).willReturn(Optional.of(customer));
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customerId);
        customerDTO.setName("mohamed");
        customerDTO.setEmail("mohamed@gmail.com");
        given(bankAccountMapper.fromCustomer(customer)).willReturn(customerDTO);
        //WHEN
        CustomerDTO customerDTO1 = underTest.getCustomerById(customerId);
        //THEN
        assertThat(customerDTO1).isEqualToComparingFieldByField(customerDTO);
    }
    @Test
    void itShouldThrowWhenCustomerNotFound() throws CustomerNotFoundException {
        //GIVEN
        long customerId= 1L;

        given(customerRepository.findById(customerId)).willReturn(Optional.empty());
        //WHEN
        //THEN
        assertThatThrownBy(()-> underTest.getCustomerById(customerId))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining("Customer Not Found");
        then(bankAccountMapper).shouldHaveNoInteractions();
    }
    @Test
    void itShouldUpdateCustomer() throws CustomerNotFoundException {
        //GIVEN
        Customer customer = new Customer(1L,
                "mohamed",
                "mohamed@gmail.com",
                List.of());

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("mohamed");
        customerDTO.setEmail("mohamed@gmail.com");
        given(bankAccountMapper.fromCustomerDTO(customerDTO)).willReturn(customer);

        //WHEN
        underTest.updateCustomer(customerDTO);
        //THEN
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        assertThat(customerArgumentCaptor.getValue()).isEqualToComparingFieldByField(customer);
    }

    @Test
    void itShouldDeleteCustomer() throws CustomerNotFoundException {
        //GIVEN
        long customerId= 1L;
        //WHEN
        underTest.deleteCustomer(customerId);
        //THEN
        ArgumentCaptor<Long> customerIDArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        then(customerRepository).should().deleteById(customerIDArgumentCaptor.capture());
        assertThat(customerIDArgumentCaptor.getValue()).isEqualTo(1L);
        then(customerRepository).shouldHaveNoMoreInteractions();

    }
}