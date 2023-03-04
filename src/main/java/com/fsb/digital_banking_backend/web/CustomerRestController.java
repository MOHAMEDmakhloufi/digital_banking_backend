package com.fsb.digital_banking_backend.web;

import com.fsb.digital_banking_backend.dtos.CustomerDTO;
import com.fsb.digital_banking_backend.entities.Customer;
import com.fsb.digital_banking_backend.exceptions.CustomerNotFoundException;
import com.fsb.digital_banking_backend.services.BankAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("")
    public List<CustomerDTO> customers(){
        return bankAccountService.listOfCustomers();
    }
    @GetMapping("/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id")  Long customerId)
            throws CustomerNotFoundException {

        return bankAccountService.getCustomerById(customerId);
    }
    @PostMapping("/save")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        return bankAccountService.saveCustomer(customerDTO);
    }
    @PutMapping("/put/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable Long customerId, @RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException {
        customerDTO.setId(customerId);
        return bankAccountService.updateCustomer(customerDTO);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
        bankAccountService.deleteCustomer(customerId);
    }
}
