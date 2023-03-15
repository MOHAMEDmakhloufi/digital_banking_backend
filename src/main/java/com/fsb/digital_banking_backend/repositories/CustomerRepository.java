package com.fsb.digital_banking_backend.repositories;

import com.fsb.digital_banking_backend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findCustomerByNameContains(String keyWord);
}
