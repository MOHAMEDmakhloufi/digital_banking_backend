package com.fsb.digital_banking_backend.web;

import com.fsb.digital_banking_backend.dtos.CustomerDTO;
import com.fsb.digital_banking_backend.entities.Customer;
import com.fsb.digital_banking_backend.exceptions.CustomerNotFoundException;
import com.fsb.digital_banking_backend.services.BankAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customers")
@Slf4j

public class CustomerRestController {
    private final BankAccountService bankAccountService;
    @Autowired
    public CustomerRestController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }
    @GetMapping("/search")
    @PostAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<CustomerDTO> searchCustomers(@RequestParam(name = "keyword", defaultValue = "") String keyWord){
        return bankAccountService.searchCustomers(keyWord);
    }
    @GetMapping("")
    @PostAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<CustomerDTO> customers(){
        return bankAccountService.listOfCustomers();
    }
    @GetMapping("/{id}")
    @PostAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public CustomerDTO getCustomer(@PathVariable(name = "id")  Long customerId)
            throws CustomerNotFoundException {

        return bankAccountService.getCustomerById(customerId);
    }
    @PostMapping("/save")
    @PostAuthorize("hasAuthority('ADMIN')")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        return bankAccountService.saveCustomer(customerDTO);
    }
    @PutMapping("/put/{customerId}")
    @PostAuthorize("hasAuthority('ADMIN')")
    public CustomerDTO updateCustomer(@PathVariable Long customerId, @RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException {
        customerDTO.setId(customerId);
        return bankAccountService.updateCustomer(customerDTO);
    }
    @DeleteMapping("/delete/{id}")
    @PostAuthorize("hasAuthority('ADMIN')")
    public void deleteCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
        bankAccountService.deleteCustomer(customerId);
    }
}
