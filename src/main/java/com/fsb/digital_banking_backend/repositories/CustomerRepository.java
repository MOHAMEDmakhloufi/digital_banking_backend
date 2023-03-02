package com.fsb.digital_banking_backend.repositories;

import com.fsb.digital_banking_backend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
